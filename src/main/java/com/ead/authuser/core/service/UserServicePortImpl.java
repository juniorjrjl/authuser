package com.ead.authuser.core.service;

import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserInsertDomain;
import com.ead.authuser.core.domain.UserUpdateDomain;
import com.ead.authuser.core.exception.DuplicatedDataException;
import com.ead.authuser.core.exception.IncorrectPasswordException;
import com.ead.authuser.core.port.RoleQueryServicePort;
import com.ead.authuser.core.port.UserEventPublisherPort;
import com.ead.authuser.core.port.UserPersistencePort;
import com.ead.authuser.core.port.UserQueryServicePort;
import com.ead.authuser.core.port.UserServicePort;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static com.ead.authuser.core.domain.enumeration.ActionType.CREATE;
import static com.ead.authuser.core.domain.enumeration.ActionType.DELETE;
import static com.ead.authuser.core.domain.enumeration.ActionType.UPDATE;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_INSTRUCTOR;
import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_STUDENT;
import static com.ead.authuser.core.domain.enumeration.UserStatus.ACTIVE;
import static com.ead.authuser.core.domain.enumeration.UserType.STUDENT;

@AllArgsConstructor
public class UserServicePortImpl implements UserServicePort {

    private final UserPersistencePort userPersistencePort;
    private final UserQueryServicePort userQueryServicePort;
    private final UserEventPublisherPort userEventPublisherPort;
    private final RoleQueryServicePort roleQueryServicePort;

    @Override
    public void delete(final UserDomain userDomain) {
        userPersistencePort.delete(userDomain);
    }

    @Override
    public void deleteAndPublish(final UUID id) {
        var domain = userQueryServicePort.findById(id);
        delete(domain);
        userEventPublisherPort.publish(domain, DELETE);
    }

    @Override
    public UserDomain save(final UserDomain domain) {
        return userPersistencePort.save(domain);
    }

    @Override
    public UserDomain saveAndPublish(final UserInsertDomain insertDomain) {
        if (userQueryServicePort.existsByUsername(insertDomain.username())){
            throw new DuplicatedDataException("Username is already taken!");
        }
        if (userQueryServicePort.existsByEmail(insertDomain.email())){
            throw new DuplicatedDataException("Email is already taken!");
        }
        var domain = UserDomain.builder()
                .username(insertDomain.username())
                .email(insertDomain.email())
                .password(insertDomain.password())
                .fullName(insertDomain.fullName())
                .userStatus(ACTIVE)
                .userType(STUDENT)
                .phoneNumber(insertDomain.phoneNumber())
                .cpf(insertDomain.cpf())
                .creationDate(OffsetDateTime.now())
                .lastUpdateDate(OffsetDateTime.now())
                .roles(Set.of(roleQueryServicePort.findByRoleName(ROLE_STUDENT)))
                .build();
        var saved = save(domain);
        userEventPublisherPort.publish(saved, CREATE);
        return saved;
    }

    @Override
    public UserDomain updateAndPublish(final UUID id, final UserUpdateDomain updateDomain) {
        var domain = userQueryServicePort.findById(id);
        domain = domain.toBuilder()
                .fullName(updateDomain.fullName())
                .phoneNumber(updateDomain.phoneNumber())
                .cpf(updateDomain.cpf())
                .lastUpdateDate(OffsetDateTime.now())
                .build();
        var saved = save(domain);
        userEventPublisherPort.publish(saved, UPDATE);
        return saved;
    }

    @Override
    public UserDomain updatePassword(final UUID id, final String oldPassword, final String newPassword) {
        var domain = userQueryServicePort.findById(id);
        if (domain.password().equals(oldPassword)){
            throw new IncorrectPasswordException("Mismatched old password");
        }
        var saved = save(domain.toBuilder().password(newPassword).lastUpdateDate(OffsetDateTime.now()).build());
        userEventPublisherPort.publish(saved, UPDATE);
        return saved;
    }

    @Override
    public UserDomain updateImage(final UUID id, final String imageUrl) {
        var domain = userQueryServicePort.findById(id);
        var saved = save(domain.toBuilder().imageUrl(imageUrl).lastUpdateDate(OffsetDateTime.now()).build());
        userEventPublisherPort.publish(saved, UPDATE);
        return saved;
    }

    @Override
    public UserDomain setUserLikeInstructor(final UUID id) {
        var user = userQueryServicePort.findById(id);
        var roleInstructor = roleQueryServicePort.findByRoleName(ROLE_INSTRUCTOR);
        var roles = user.roles();
        roles.add(roleInstructor);
        user = user.toBuilder().roles(roles).lastUpdateDate(OffsetDateTime.now()).build();
        var saved = save(user);
        userEventPublisherPort.publish(saved, UPDATE);
        return saved;
    }
}
