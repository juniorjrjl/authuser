package com.ead.authuser.adapter.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.servlet.DispatcherType.ERROR;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_ADMIN;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_INSTRUCTOR;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_STUDENT;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_USER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtProvider jwtProvider;


    private static final String[] AUTH_WITHE_LIST = {"/auth/**"};

    @Bean
    public AuthenticationJwtFilter authenticationJwtFilter(){
        return new AuthenticationJwtFilter(jwtProvider, userDetailsService);
    }

    @Bean
    protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(ERROR).permitAll()
                        .requestMatchers(AUTH_WITHE_LIST).permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler(){
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        var roleHierarchy = new RoleHierarchyImpl();
        var hierarchy = "%s > %s \n %s > %s \n %s > %s".formatted(ROLE_ADMIN.toString(), ROLE_INSTRUCTOR.toString(),
                ROLE_INSTRUCTOR.toString(), ROLE_STUDENT.toString(), ROLE_STUDENT.toString(), ROLE_USER.toString());
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
