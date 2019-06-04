package com.is5pz3.monitor.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthorizationData {
    private String login;
    private String token;

    @JsonProperty("valid_through")
    private Date validThrough;
}
