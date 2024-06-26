package com.web.careus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CampaignTransactionsHistoryResponse {
    private long id;
    private String username;
    private double transactionAmount;
    private String message;
    private LocalDateTime transactionDate;
}
