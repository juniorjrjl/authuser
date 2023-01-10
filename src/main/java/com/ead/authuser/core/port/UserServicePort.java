package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.domain.UserInsertDomain;
import com.ead.authuser.core.domain.UserUpdateDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServicePort {

    void delete(final UserDomain userDomain);

    void deleteAndPublish(final UUID id);

    UserDomain save(final UserDomain userDomain);

    UserDomain saveAndPublish(final UserInsertDomain insertDomain);

    UserDomain updateAndPublish(final UUID id, final UserUpdateDomain updateDomain);

    UserDomain updatePassword(final UUID id, final String oldPassword, final String newPassword);

    UserDomain updateImage(final UUID id, final String imageUrl);

    UserDomain setUserLikeInstructor(final UUID id);

}
