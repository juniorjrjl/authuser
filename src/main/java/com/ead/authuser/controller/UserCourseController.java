package com.ead.authuser.controller;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.UserCourseDTO;
import com.ead.authuser.service.UserCourseService;
import com.ead.authuser.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping
@AllArgsConstructor
@Log4j2
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;

    private final UserCourseService userCourseService;

    @GetMapping("users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllByUser(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
                                                        @PathVariable final UUID userId){
        return ResponseEntity.status(OK).body(courseClient.getAllCoursesByUser(pageable, userId));
    }

    @PostMapping("users/{userId}/courses/subscription")
    public ResponseEntity<Object> subscriptionInCourse(@PathVariable final UUID userId, @RequestBody @Valid final UserCourseDTO request){
        var optionalModel = userService.findById(userId);
        if (optionalModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found!");
        }
        var userModel = optionalModel.get();
        if (userCourseService.existsByUserAndCourseId(userModel, request.getCourseId())){
            return ResponseEntity.status(CONFLICT).body("Error: subscription already exists");
        }
        var userCourseModel = userCourseService.save(userModel.toUserCourseModel(request.getCourseId()));
        return ResponseEntity.status(CREATED).body(userCourseModel);
    }

    @DeleteMapping("users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable final UUID courseId){
        if (!userCourseService.existsByCourseId(courseId)){
            return ResponseEntity.status(NOT_FOUND).body("User not found!");
        }
        userCourseService.deleteUserCourseByCourse(courseId);
        return ResponseEntity.status(OK).body("UserCourse deleted successfully.");
    }

}
