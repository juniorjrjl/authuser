package com.ead.authuser.service;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId);

    UserCourseModel save(UserCourseModel userCourseModel);
}
