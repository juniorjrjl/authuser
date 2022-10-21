package com.ead.authuser.controller;

import com.ead.authuser.config.security.JwtProvider;
import com.ead.authuser.dto.JwtDTO;
import com.ead.authuser.dto.LoginDTO;
import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.RoleService;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Value;
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

import static com.ead.authuser.enumeration.RoleType.ROLE_STUDENT;
import static com.ead.authuser.enumeration.UserStatus.ACTIVE;
import static com.ead.authuser.enumeration.UserType.STUDENT;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("auth")
@AllArgsConstructor
@Log4j2
public class AuthenticationController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("signup")
    public ResponseEntity<Object> register(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                               @Validated(UserDto.UserView.RegistrationPost.class)  final UserDto request){
        log.debug("[POST] [register] userDto received {}", request);
        if (userService.existsByUsername(request.getUsername())){
            log.warn("[POST] [register] Username {} is already taken", request.getUsername());
            return ResponseEntity.status(CONFLICT).body("Error: Username is already taken!");
        }
        if (userService.existsByEmail(request.getUsername())){
            log.warn("[POST] [register] Email {} is already taken", request.getEmail());
            return ResponseEntity.status(CONFLICT).body("Error: Email is already taken!");
        }
        var roleModel = roleService.findByRoleName(ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var model = new UserModel();
        BeanUtils.copyProperties(request, model);
        model.setUserStatus(ACTIVE);
        model.setUserType(STUDENT);
        model.setCreationDate(OffsetDateTime.now());
        model.setLastUpdateDate(OffsetDateTime.now());
        model.getRoles().add(roleModel);
        model = userService.saveAndPublish(model);
        log.debug("[POST] [register] userModel saved {}", model);
        log.info("[POST] [register] user saved successfully id {}", model.getId());
        return ResponseEntity.status(CREATED).body(model);
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
