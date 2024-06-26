package com.web.careus.controller;

import com.web.careus.dto.request.TransactionRequest;
import com.web.careus.model.transaction.Transaction;
import com.web.careus.security.jwt.JwtUtils;
import com.web.careus.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/transaction/{transactionType}/{code}")
    public ResponseEntity<?> createTransaction(@PathVariable String transactionType,
                                               @PathVariable String code,
                                               @RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction transaction = transactionService.createTransaction(transactionType, code, transactionRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException | BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/inquiry")
//    public String inquiry(@RequestParam String token) {
//        return transactionService.handleInquiry(token);
//    }
//
//    @PostMapping("/payment")
//    public String payment(@RequestParam String token) {
//        return transactionService.handlePayment(token);
//    }
//
//    @PostMapping("/reversal")
//    public String reversal(@RequestParam String token) {
//        return transactionService.handleReversal(token);
//    }



    @GetMapping("/total-transaction-count")
    public double getTotalTransactionCount() {
        return transactionService.getTotalTransactionCount();
    }

    @GetMapping("/admin/get-all-transactions")
    public ResponseEntity<Page<Transaction>> getAllTransactions(@RequestParam(name = "year", required = false) Integer year,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        if (year == null) {
            year = Year.now().getValue();
        }

        Page<Transaction> transactions = transactionService.getAllTransaction(year, pageRequest);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/transaction/get-zakat")
    public ResponseEntity<Page<Transaction>> getZakatTransaction(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<Transaction> transactions = transactionService.getZakatTransaction(pageRequest);
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/transaction/get-infak")
    public ResponseEntity<Page<Transaction>> getInfakTransaction(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<Transaction> transactions = transactionService.getInfakTransaction(pageRequest);
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/transaction/get-wakaf")
    public ResponseEntity<Page<Transaction>> getWakafTransaction(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<Transaction> transactions = transactionService.getWakafTransaction(pageRequest);
        return ResponseEntity.ok().body(transactions);
    }
}