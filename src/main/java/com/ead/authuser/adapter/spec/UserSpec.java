package com.ead.authuser.adapter.spec;

import com.ead.authuser.adapter.dto.UserFilterDTO;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserSpec {

    public static Specification<UserEntity> findAllFilter(final UserFilterDTO filter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(filter.userType())){
                predicates.add(criteriaBuilder.equal(root.get(UserEntity_.userType), filter.userType()));
            }
            if (Objects.nonNull(filter.userStatus())){
                predicates.add(criteriaBuilder.equal(root.get(UserEntity_.userStatus), filter.userStatus()));
            }
            if (StringUtils.isNotBlank(filter.email())){
                predicates.add(criteriaBuilder.like(root.get(UserEntity_.email), filter.likeEmail()));
            }
            if (StringUtils.isNotBlank(filter.fullName())){
                predicates.add(criteriaBuilder.like(root.get(UserEntity_.fullName), filter.likeFullName()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
