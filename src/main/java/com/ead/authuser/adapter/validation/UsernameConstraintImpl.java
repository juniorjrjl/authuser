package com.ead.authuser.adapter.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameConstraint, String> {

    @Override
    public void initialize(final UsernameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return Objects.nonNull(username) && StringUtils.isNotBlank(username) && StringUtils.containsNone(username, " ");
    }

}
