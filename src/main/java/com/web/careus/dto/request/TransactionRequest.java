package com.web.careus.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class TransactionRequest {
    private String username;
    private String phoneNumber;
    private double amount;
    private String message;
    private Set<String> role;
}
