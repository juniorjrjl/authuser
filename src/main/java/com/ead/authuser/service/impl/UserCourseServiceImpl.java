package com.ead.authuser.service.impl;

import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

}
