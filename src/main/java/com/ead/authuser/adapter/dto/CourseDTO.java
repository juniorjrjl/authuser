package com.ead.authuser.adapter.dto;

import com.ead.authuser.core.domain.enumeration.CourseLevel;
import com.ead.authuser.core.domain.enumeration.CourseStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDTO {

    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private UUID userInstructor;
    private CourseLevel courseLevel;

}
