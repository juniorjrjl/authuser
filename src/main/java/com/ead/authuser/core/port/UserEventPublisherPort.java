package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.enumeration.ActionType;

public interface UserEventPublisherPort {

    void publish(final UserDomain domain, final ActionType actionType);

}
