package com.ead.authuser.adapter.mapper;

import com.ead.authuser.adapter.dto.UserDto;
import com.ead.authuser.adapter.dto.UserEventDTO;
import com.ead.authuser.adapter.dto.UserFilterDTO;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.domain.UserInsertDomain;
import com.ead.authuser.core.domain.UserUpdateDomain;
import com.ead.authuser.core.domain.enumeration.ActionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    UserEntity toEntity(final UserDomain domain);

    UserDomain toDomain(final UserEntity entity);

    @Mapping(target = "actionType", source = "actionType")
    UserEventDTO toEventDTO(final UserDomain domain, final ActionType actionType);

    UserFilterDomain toDomain(final UserFilterDTO dto);

    UserFilterDTO toDomain(final UserFilterDomain domain);

    UserUpdateDomain toUpdateDomain(final UserDto entity);

    UserInsertDomain toInsertDomain(final UserDto dto);

}
