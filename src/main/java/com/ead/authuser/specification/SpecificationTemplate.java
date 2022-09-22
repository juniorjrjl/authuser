package com.ead.authuser.specification;

import com.ead.authuser.model.UserCourseModel_;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.model.UserModel_;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = UserModel_.USER_TYPE, spec = Equal.class),
            @Spec(path = UserModel_.USER_STATUS, spec = Equal.class),
            @Spec(path = UserModel_.EMAIL, spec = Like.class),
            @Spec(path = UserModel_.FULL_NAME, spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel> {}

    public static Specification<UserModel> userCourseId(final UUID courseId){
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            var userProd = root.join(UserModel_.usersCourses);
            return criteriaBuilder.equal(userProd.get(UserCourseModel_.courseId), courseId);
        };
    }

}
