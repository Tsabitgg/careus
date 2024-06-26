package com.web.careus.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String username;
    private String phoneNumber;
    private String password;

    private String address;
    private String role;
}