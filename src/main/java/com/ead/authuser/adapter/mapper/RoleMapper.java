package com.ead.authuser.adapter.mapper;

import com.ead.authuser.adapter.outbound.persistence.entity.RoleEntity;
import com.ead.authuser.core.domain.RoleDomain;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RoleMapper {

    RoleDomain toDomain(final RoleEntity entity);

    RoleEntity toEntity(final RoleDomain domain);

}
