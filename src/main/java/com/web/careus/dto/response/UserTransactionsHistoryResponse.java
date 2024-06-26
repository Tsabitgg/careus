package com.web.careus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserTransactionsHistoryResponse {
    private String username;
    private String type;
    private String transactionName;
    private double transactionAmount;
    private String message;
    private LocalDateTime transactionDate;
    private boolean success;
}
