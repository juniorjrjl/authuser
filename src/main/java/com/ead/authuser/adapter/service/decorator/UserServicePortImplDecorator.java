package com.ead.authuser.adapter.service.decorator;

import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserInsertDomain;
import com.ead.authuser.core.domain.UserUpdateDomain;
import com.ead.authuser.core.port.UserServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserServicePortImplDecorator implements UserServicePort {

    private final UserServicePort userServicePort;

    @Transactional
    @Override
    public void delete(final UserDomain userDomain) {
        userServicePort.delete(userDomain);
    }

    @Transactional
    @Override
    public void deleteAndPublish(final UUID id) {
        userServicePort.deleteAndPublish(id);
    }

    @Transactional
    @Override
    public UserDomain save(final UserDomain domain) {
        return userServicePort.save(domain);
    }

    @Transactional
    @Override
    public UserDomain saveAndPublish(final UserInsertDomain insertDomain) {
        return userServicePort.saveAndPublish(insertDomain);
    }

    @Transactional
    @Override
    public UserDomain updateAndPublish(final UUID id, final UserUpdateDomain updateDomain) {
        return userServicePort.updateAndPublish(id, updateDomain);
    }

    @Transactional
    @Override
    public UserDomain updatePassword(final UUID id, final String oldPassword, final String newPassword) {
        return userServicePort.updatePassword(id, oldPassword, newPassword);
    }

    @Transactional
    @Override
    public UserDomain updateImage(final UUID id, final String imageUrl) {
        return userServicePort.updateImage(id, imageUrl);
    }

    @Transactional
    @Override
    public UserDomain setUserLikeInstructor(final UUID id) {
        return userServicePort.setUserLikeInstructor(id);
    }
}
