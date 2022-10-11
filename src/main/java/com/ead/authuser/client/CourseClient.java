package com.ead.authuser.client;

import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.ResponsePageDTO;
import com.ead.authuser.service.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.GET;

@Component
@Log4j2
public class CourseClient {

    private final String requestUrl;
    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    public CourseClient(@Value("${ead.api.url.course}") final String requestUrl,
                        final RestTemplate restTemplate,
                        final UtilsService utilsService) {
        this.requestUrl = requestUrl;
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

    //@Retry(name = "retryInstance", fallbackMethod = "retryFallback")
    //@CircuitBreaker(name = "circuitbreakerInstance", fallbackMethod = "retryCircuitBreaker")
    public Page<CourseDTO> getAllCoursesByUser(final Pageable pageable, final UUID userId){
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        var url = requestUrl + utilsService.createUrl(pageable, userId);
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

    public Page<CourseDTO> retryFallback(final Pageable pageable, final UUID userId, final Throwable throwable ){
        log.error("Inside retry retryFallback cause - {}", throwable.toString(), throwable);
        List<CourseDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    public Page<CourseDTO> retryCircuitBreaker(final Pageable pageable, final UUID userId, final Throwable throwable ){
        log.error("Inside circuit breaker fallback cause - {}", throwable.toString(), throwable);
        List<CourseDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

}
