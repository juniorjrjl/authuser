package com.ead.authuser.repository;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>, JpaSpecificationExecutor<UserModel> {


    boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId);

}
