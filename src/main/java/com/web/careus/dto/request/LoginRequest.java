package com.web.careus.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}