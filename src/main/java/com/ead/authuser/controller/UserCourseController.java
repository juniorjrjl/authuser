package com.ead.authuser.controller;

import com.ead.authuser.client.UserClient;
import com.ead.authuser.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("users/{userId}/courses")
@AllArgsConstructor
@Log4j2
public class UserCourseController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getAllByUser(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
                                                        @PathVariable final UUID userId){
        return ResponseEntity.status(OK).body(userClient.getAllCoursesByUser(pageable, userId));
    }

}
