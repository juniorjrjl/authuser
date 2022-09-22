package com.ead.authuser.service;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String createUrl(final Pageable pageable, final UUID userId);

}
