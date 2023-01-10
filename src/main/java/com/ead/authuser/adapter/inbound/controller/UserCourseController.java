package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.dto.CourseDTO;
import com.ead.authuser.adapter.mapper.CourseMapper;
import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.port.CourseClientPort;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping
@AllArgsConstructor
@Log4j2
public class UserCourseController {

    private final CourseClientPort courseClientPort;
    private final CourseMapper courseMapper;

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("users/{userId}/courses")
    public Page<CourseDTO> getAllByUser(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
                                                        @PathVariable final UUID userId,
                                                        @RequestHeader("Authorization") final String token){
        var pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize());
        var courses = courseClientPort.getAllCoursesByUser(pageInfo, userId, token).stream().map(courseMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(courses, pageable, courses.size());
    }

}
