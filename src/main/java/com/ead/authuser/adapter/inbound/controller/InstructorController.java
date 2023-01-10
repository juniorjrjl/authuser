package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.mapper.RoleMapper;
import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.port.RoleQueryServicePort;
import com.ead.authuser.core.port.UserQueryServicePort;
import com.ead.authuser.core.port.UserServicePort;
import com.ead.authuser.adapter.dto.InstructorDTO;
import com.ead.authuser.core.domain.enumeration.UserType;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.time.OffsetDateTime;

import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_INSTRUCTOR;
import static com.ead.authuser.core.domain.enumeration.UserType.INSTRUCTOR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("instructors")
@AllArgsConstructor
@Log4j2
public class InstructorController {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("subscription")
    public UserEntity saveSubscriptionInstructor(@RequestBody @Valid final InstructorDTO request){
        var response = userServicePort.setUserLikeInstructor(request.getId());
        return userMapper.toEntity(response);
    }

}
