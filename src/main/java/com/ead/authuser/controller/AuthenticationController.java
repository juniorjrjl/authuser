package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

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
        var model = new UserModel();
        BeanUtils.copyProperties(request, model);
        model.setUserStatus(ACTIVE);
        model.setUserType(STUDENT);
        model.setCreationDate(OffsetDateTime.now());
        model.setLastUpdateDate(OffsetDateTime.now());
        model = userService.save(model);
        log.debug("[POST] [register] userModel saved {}", model);
        log.info("[POST] [register] user saved successfully id {}", model.getId());
        return ResponseEntity.status(CREATED).body(model);
    }

}
