package com.ead.authuser.service.impl;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseClient courseClient;

    @Override
    public Page<UserModel> findAll(final Specification<UserModel> spec, final Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<UserModel> findById(final UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(final UserModel model) {
        var deleteUserCourseInCourse = false;
        var usersCourses = userCourseRepository.findAllUserCourseIntoUser(model.getId());
        if (CollectionUtils.isNotEmpty(usersCourses)){
            deleteUserCourseInCourse = true;
            userCourseRepository.deleteAll(usersCourses);
        }
        userRepository.delete(model);
        if(deleteUserCourseInCourse){
            courseClient.deleteUserInCourse(model.getId());
        }
    }

    @Override
    public UserModel save(final UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
}
