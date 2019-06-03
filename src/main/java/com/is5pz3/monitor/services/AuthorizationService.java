package com.is5pz3.monitor.services;

import com.is5pz3.monitor.exceptions.UnauthorizedException;
import com.is5pz3.monitor.model.data.AuthorizationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Value("${authorizationService.baseUrl}")
    private String baseUrl;

    public String getUserLogin(String token) {
        String path = "/users/auth?authToken={token}";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AuthorizationData> responseEntity = restTemplate.exchange(baseUrl + path,
                HttpMethod.GET,
                null,
                AuthorizationData.class,
                token
        );

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw getUnauthorizedException(token);
        }

        AuthorizationData authorizationData = responseEntity.getBody();
        if (authorizationData == null) {
            throw getUnauthorizedException(token);
        }
        return Optional.ofNullable(authorizationData.getLogin())
                .orElseThrow(() -> getUnauthorizedException(token));
    }

    private UnauthorizedException getUnauthorizedException(String token) {
        return new UnauthorizedException("No valid user for token: " + token);
    }

}
