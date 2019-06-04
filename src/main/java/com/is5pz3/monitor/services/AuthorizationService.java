package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.UnauthorizedException;
import com.is5pz3.monitor.model.data.AuthorizationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Value("${authorizationService.baseUrl}")
    private String baseUrl;

    public String getUserLogin(String token) {
        String path = "/users/auth?authToken=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        System.out.println(baseUrl + path);
        try {
            ResponseEntity<AuthorizationData> responseEntity = restTemplate.exchange(baseUrl + path,
                    HttpMethod.GET,
                    entity,
                    AuthorizationData.class
            );

            AuthorizationData authorizationData = responseEntity.getBody();
            if (authorizationData == null) {
                throw getUnauthorizedException(token);
            }
            return Optional.ofNullable(authorizationData.getLogin())
                    .orElseThrow(() -> getUnauthorizedException(token));
        } catch (Exception e) {
            throw getUnauthorizedException(token);
        }
    }

    private UnauthorizedException getUnauthorizedException(String token) {
        return new UnauthorizedException("No valid user for token: " + token);
    }

}
