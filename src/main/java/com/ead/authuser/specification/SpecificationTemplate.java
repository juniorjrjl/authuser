package com.ead.authuser.specification;

import com.ead.authuser.model.UserModel;
import com.ead.authuser.model.UserModel_;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    @And({
            @Spec(path = UserModel_.USER_TYPE, spec = Equal.class),
            @Spec(path = UserModel_.USER_STATUS, spec = Equal.class),
            @Spec(path = UserModel_.EMAIL, spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel> {}

}
