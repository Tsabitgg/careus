package com.web.careus.dto.response;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private long id;
    private String username;
    private String phoneNumber;
    private List<String> roles;

    public JwtResponse(String accessToken, long id, String username, String phoneNumber, List<String> userRoles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.roles = userRoles;
    }
}
