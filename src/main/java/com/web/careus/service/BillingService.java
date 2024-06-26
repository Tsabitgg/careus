package com.web.careus.service;

import com.web.careus.dto.request.TransactionRequest;
import com.web.careus.model.transaction.Billing;
import org.apache.coyote.BadRequestException;

public interface BillingService {
    Billing createBilling(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException;

    boolean getBillingSuccess(Long billingId);
}
