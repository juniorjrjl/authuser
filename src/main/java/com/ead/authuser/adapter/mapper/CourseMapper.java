package com.ead.authuser.adapter.mapper;

import com.ead.authuser.adapter.dto.CourseDTO;
import com.ead.authuser.core.domain.CourseDomain;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CourseMapper {

    CourseDomain toDomain(final CourseDTO dto);

    CourseDTO toDTO(final CourseDomain domain);

}
