package com.eugenescobich.outbox.pattern.utils;

import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestUtils {

    public static final String SERVER_URL_TEMPLATE = "http://localhost:%s";

    private final TestRestTemplate restTemplate;

    @Setter
    private int port;

    public <T> T doGet(
        Class<T> clazz,
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        String url = getUrl(urlTemplate, urlTemplateParameters);
        log.debug("GET Request url: {}", url);
        HttpEntity<Object> requestEntity = getHttpEntityWithSecurityHeaders(null);
        log.debug("GET Request headers: {}", requestEntity);
        Instant start = Instant.now();
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            clazz
        );
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.debug("GET Response: {}", response.toString());
        log.debug("GET Execution time: {}", duration.toString());
        return response.getBody();
    }

    public <T> T doPost(
        Object payload,
        Class<T> clazz,
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        return doExchange(payload, clazz, urlTemplate, HttpMethod.POST, urlTemplateParameters);
    }

    public <T> T doPut(
        Object payload,
        Class<T> clazz,
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        return doExchange(payload, clazz, urlTemplate, HttpMethod.PUT, urlTemplateParameters);
    }

    public <T> T doPatch(
        Object payload,
        Class<T> clazz,
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        return doExchange(payload, clazz, urlTemplate, HttpMethod.PATCH, urlTemplateParameters);
    }

    public <T> T doDelete(
        Object payload,
        Class<T> clazz,
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        return doExchange(payload, clazz, urlTemplate, HttpMethod.DELETE, urlTemplateParameters);
    }

    public <T> T doExchange(
        Object payload,
        Class<T> clazz,
        String urlTemplate,
        HttpMethod httpMethod,
        Object... urlTemplateParameters
    ) {
        String url = getUrl(urlTemplate, urlTemplateParameters);
        log.debug("POST Request url: {}", url);
        HttpEntity requestEntity = getHttpEntityWithSecurityHeaders(payload);

        log.debug("POST Request: {}", requestEntity);
        Instant start = Instant.now();
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            httpMethod,
            requestEntity,
            clazz
        );
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.debug("POST Execution time: {}", duration.toString());
        log.debug("POST Response: {}", response);
        return response.getBody();
    }

    private String getUrl(
        String urlTemplate,
        Object... urlTemplateParameters
    ) {
        String hostAndPort = String.format(SERVER_URL_TEMPLATE, port);
        return hostAndPort + String.format(urlTemplate, urlTemplateParameters);
    }

    private HttpEntity getHttpEntityWithSecurityHeaders(Object payload) {
        return new HttpEntity<>(payload);
    }
}
