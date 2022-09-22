package com.ead.authuser.client;

import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.ResponsePageDTO;
import com.ead.authuser.service.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.GET;

@Component
@AllArgsConstructor
@Log4j2
public class UserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    public Page<CourseDTO> getAllCoursesByUser(final Pageable pageable, final UUID userId){
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        var url = utilsService.createUrl(pageable, userId);
        log.debug("Request URL : {}", url);
        log.info("Request URL : {}", url);
        try{
            var responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};
            result = restTemplate.exchange(url, GET, null, responseType);
            log.debug("Response number of elements : {}", result.getBody().getSize());
        }catch (final HttpStatusCodeException ex){
            log.error("Error request / courses ", ex);
        }
        log.info("Ending request / courses userId : {}", userId);
        return result.getBody();
    }

}
