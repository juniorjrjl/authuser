package com.ead.authuser.repository;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId);

    @Query(value = "select * from TB_USERS_COURSES where user_id = userId", nativeQuery = true)
    List<UserCourseModel> findAllUserCourseIntoUser(final UUID userId);

}
