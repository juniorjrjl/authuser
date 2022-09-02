package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
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
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<Object> register(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                               @Validated(UserDto.UserView.RegistrationPost.class)  final UserDto request){
        if (userService.existsByUsername(request.getUsername())){
            return ResponseEntity.status(CONFLICT).body("Error: Username is already taken!");
        }
        if (userService.existsByEmail(request.getUsername())){
            return ResponseEntity.status(CONFLICT).body("Error: Email is already taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(request, userModel);
        userModel.setUserStatus(ACTIVE);
        userModel.setUserType(STUDENT);
        userModel.setCreationDate(OffsetDateTime.now());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        userModel = userService.save(userModel);
        return ResponseEntity.status(CREATED).body(userModel);
    }

}
