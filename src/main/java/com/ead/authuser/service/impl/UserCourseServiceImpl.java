package com.ead.authuser.service.impl;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    @Override
    public boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(final UserCourseModel userCourseModel) {
        return userCourseRepository.save(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(final UUID courseId) {
        return userCourseRepository.existsByCourseId(courseId);
    }

    @Transactional
    @Override
    public void deleteUserCourseByCourse(final UUID courseId) {
        userCourseRepository.deleteAllByCourseId(courseId);
    }

}
