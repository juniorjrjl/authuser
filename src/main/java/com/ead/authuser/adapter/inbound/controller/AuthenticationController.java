package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.config.security.JwtProvider;
import com.ead.authuser.adapter.mapper.RoleMapper;
import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.port.RoleServicePort;
import com.ead.authuser.core.port.UserServicePort;
import com.ead.authuser.adapter.dto.JwtDTO;
import com.ead.authuser.adapter.dto.LoginDTO;
import com.ead.authuser.adapter.dto.UserDto;
import com.ead.authuser.core.domain.enumeration.RoleType;
import com.ead.authuser.core.domain.enumeration.UserStatus;
import com.ead.authuser.core.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.OffsetDateTime;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("auth")
@AllArgsConstructor
@Log4j2
public class AuthenticationController {

    private final UserServicePort userServicePort;
    private final RoleServicePort roleServicePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @PostMapping("signup")
    public ResponseEntity<Object> register(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                               @Validated(UserDto.UserView.RegistrationPost.class)  final UserDto request){
        log.debug("[POST] [register] userDto received {}", request);
        if (userServicePort.existsByUsername(request.getUsername())){
            log.warn("[POST] [register] Username {} is already taken", request.getUsername());
            return ResponseEntity.status(CONFLICT).body("Error: Username is already taken!");
        }
        if (userServicePort.existsByEmail(request.getUsername())){
            log.warn("[POST] [register] Email {} is already taken", request.getEmail());
            return ResponseEntity.status(CONFLICT).body("Error: Email is already taken!");
        }
        var roleModel = roleServicePort.findByRoleName(RoleType.ROLE_STUDENT).map(roleMapper::toEntity)
                .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var model = new UserEntity();
        BeanUtils.copyProperties(request, model);
        model.setUserStatus(UserStatus.ACTIVE);
        model.setUserType(UserType.STUDENT);
        model.setCreationDate(OffsetDateTime.now());
        model.setLastUpdateDate(OffsetDateTime.now());
        model.getRoles().add(roleModel);
        var user = userServicePort.saveAndPublish(userMapper.toDomain(model));
        var response = userMapper.toEntity(user);
        log.debug("[POST] [register] userModel saved {}", response);
        log.info("[POST] [register] user saved successfully id {}", response.getId());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("login")
    public ResponseEntity<JwtDTO> authenticate(@Valid @RequestBody final LoginDTO request){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDTO(jwt));
    }


}
