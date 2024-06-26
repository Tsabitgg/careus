package com.web.careus.dto.response;

import com.web.careus.model.transaction.Transaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private long transactionId;
    private String username;
    private String phoneNumber;
    private double transactionAmount;
    private String message;
    private LocalDateTime transactionDate;
    private boolean success;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.username = transaction.getUsername();
        this.phoneNumber = transaction.getPhoneNumber();
        this.transactionAmount = transaction.getTransactionAmount();
        this.message = transaction.getMessage();
        this.transactionDate = transaction.getTransactionDate();
        this.success = transaction.isSuccess();
    }
}
