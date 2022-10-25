package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.CourseDomain;
import com.ead.authuser.core.domain.PageInfo;

import java.util.List;
import java.util.UUID;

public interface CourseClientPort {

    List<CourseDomain> getAllCoursesByUser(final PageInfo pageInfo, final UUID userId, final String token);

}
