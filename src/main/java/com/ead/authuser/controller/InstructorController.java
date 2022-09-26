package com.ead.authuser.controller;

import com.ead.authuser.dto.InstructorDTO;
import com.ead.authuser.enumeration.UserType;
import com.ead.authuser.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.time.OffsetDateTime;

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

    @PostMapping("subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid final InstructorDTO request){
        var modelOptional = userService.findById(request.getId());
        if(modelOptional.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var model = modelOptional.get();
        model.setUserType(INSTRUCTOR);
        model.setLastUpdateDate(OffsetDateTime.now());
        model = userService.save(model);
        return ResponseEntity.status(OK).body(model);
    }

}
