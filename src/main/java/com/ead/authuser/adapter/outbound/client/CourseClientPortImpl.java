package com.ead.authuser.adapter.outbound.client;

import com.ead.authuser.adapter.dto.CourseDTO;
import com.ead.authuser.adapter.dto.ResponsePageDTO;
import com.ead.authuser.adapter.mapper.CourseMapper;
import com.ead.authuser.core.domain.CourseDomain;
import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.port.CourseClientPort;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;

@Log4j2
@Component
public class CourseClientPortImpl implements CourseClientPort {

    private final String requestUrl;
    private final RestTemplate restTemplate;

    private final CourseMapper courseMapper;

    public CourseClientPortImpl(@Value("${ead.api.url.course}") final String requestUrl,
                                final RestTemplate restTemplate, final CourseMapper courseMapper) {
        this.requestUrl = requestUrl;
        this.restTemplate = restTemplate;
        this.courseMapper = courseMapper;
    }

    private String createUrl(final Pageable pageable, final UUID userId){
        return "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
    }

    @Override
    public List<CourseDomain> getAllCoursesByUser(final PageInfo pageInfo, final UUID userId, final String token) {
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        var pageable = PageRequest.of(pageInfo.pageNumber(), pageInfo.pageSize());
        var url = requestUrl + createUrl(pageable, userId);
        var headers = new HttpHeaders();
        headers.set("Authorization", token);
        var requestEntity = new HttpEntity<>("parameters", headers);
        log.debug("Request URL : {}", url);
        log.info("Request URL : {}", url);
        var responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};
        result = restTemplate.exchange(url, GET, requestEntity, responseType);
        log.debug("Response number of elements : {}", result.getBody().getSize());
        log.info("Ending request / courses userId : {}", userId);
        return result.getBody().getContent().stream().map(courseMapper::toDomain).collect(Collectors.toList());
    }

}
