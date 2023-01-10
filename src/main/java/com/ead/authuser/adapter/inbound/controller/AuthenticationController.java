package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.config.security.JwtProvider;
import com.ead.authuser.adapter.dto.JwtDTO;
import com.ead.authuser.adapter.dto.LoginDTO;
import com.ead.authuser.adapter.dto.UserDto;
import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.port.UserServicePort;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("auth")
@AllArgsConstructor
@Log4j2
public class AuthenticationController {

    private final UserServicePort userServicePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @PostMapping("signup")
    @ResponseStatus(CREATED)
    public UserEntity register(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                               @Validated(UserDto.UserView.RegistrationPost.class)  final UserDto request){
        log.debug("[POST] [register] userDto received {}", request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var user = userServicePort.saveAndPublish(userMapper.toInsertDomain(request));
        var response = userMapper.toEntity(user);
        log.debug("[POST] [register] userModel saved {}", response);
        log.info("[POST] [register] user saved successfully id {}", response.getId());
        return response;
    }

    @PostMapping("login")
    public JwtDTO authenticate(@Valid @RequestBody final LoginDTO request){
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtProvider.generateJwt(authentication);
        return new JwtDTO(jwt);
    }


}
