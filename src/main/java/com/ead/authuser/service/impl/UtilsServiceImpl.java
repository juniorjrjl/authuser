package com.ead.authuser.service.impl;

import com.ead.authuser.service.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    private final String REQUEST_URI = "http://appcourse:8082";

    public String createUrl(final Pageable pageable, final UUID userId){
        return REQUEST_URI + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
    }

}
