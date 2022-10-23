package com.ead.authuser.controller;

import com.ead.authuser.dto.InstructorDTO;
import com.ead.authuser.service.RoleService;
import com.ead.authuser.service.UserService;
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

import static com.ead.authuser.enumeration.RoleType.ROLE_INSTRUCTOR;
import static com.ead.authuser.enumeration.UserType.INSTRUCTOR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("instructors")
@AllArgsConstructor
@Log4j2
public class InstructorController {

    private final UserService userService;
    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid final InstructorDTO request){
        var modelOptional = userService.findById(request.getId());
        if(modelOptional.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var roleModel = roleService.findByRoleName(ROLE_INSTRUCTOR).orElseThrow(() -> new RuntimeException("Error: Role not found"));
        var model = modelOptional.get();
        model.setUserType(INSTRUCTOR);
        model.setLastUpdateDate(OffsetDateTime.now());
        model.getRoles().add(roleModel);
        model = userService.updateAndPublish(model);
        return ResponseEntity.status(OK).body(model);
    }

}
