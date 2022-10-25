package com.ead.authuser.core.domain;

import com.ead.authuser.core.domain.enumeration.CourseLevel;
import com.ead.authuser.core.domain.enumeration.CourseStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public record CourseDomain(UUID id,
                           String name,
                           String description,
                           String imageUrl,
                           CourseStatus courseStatus,
                           UUID userInstructor,
                           CourseLevel courseLevel){

    @Builder(toBuilder = true)
    public CourseDomain{

    }
}
