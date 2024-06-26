package com.web.careus.controller;

import com.web.careus.dto.request.EditProfileRequest;
import com.web.careus.dto.response.ServiceOfficeResponse;
import com.web.careus.dto.response.UserTransactionsHistoryResponse;
import com.web.careus.enumeration.EServiceOffice;
import com.web.careus.model.user.User;
import com.web.careus.service.TransactionService;
import com.web.careus.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/admin/get-all-user")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/my-profile")
    public ResponseEntity<?> getCurrentUser() throws BadRequestException {
        try{
            User currentUser = userService.getCurrentUser();
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

    @GetMapping("/user/history")
    public ResponseEntity<List<UserTransactionsHistoryResponse>> getUserTransactionsHistory() throws BadRequestException {
        List<UserTransactionsHistoryResponse> userTransactionsDTO = transactionService.getUserTransactionsHistory();
        return ResponseEntity.ok(userTransactionsDTO);
    }

    @GetMapping("/user/summary")
    public ResponseEntity<Map<String, Double>> getUserTransactionSummary(@RequestParam(name = "year", required = false) Integer year) throws BadRequestException {
        Map<String, Double> summary;
        if (year == null) {
            summary = transactionService.getUserTransactionSummary();
        } else {
            summary = transactionService.getUserTransactionSummaryByYear(year);
        }
        return ResponseEntity.ok().body(summary);
    }

    @PutMapping("/user/edit-profile")
    public ResponseEntity<?> editProfile(@ModelAttribute EditProfileRequest editProfileRequest) throws BadRequestException {
        try{
            User updatedUser = userService.editProfile(editProfileRequest);
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

    @GetMapping("/serviceOffice/get-all")
    public List<ServiceOfficeResponse> getAllServiceOffice(){
        EServiceOffice[] serviceOffices = EServiceOffice.values();
        List<ServiceOfficeResponse> serviceOfficeResponses = new ArrayList<>();

        for (int i = 0; i < serviceOffices.length; i++){
            ServiceOfficeResponse serviceOfficeDTO = new ServiceOfficeResponse();
            serviceOfficeDTO.setId(i + 1);
            serviceOfficeDTO.setServiceOffice(serviceOffices[i].toString());
            serviceOfficeResponses.add(serviceOfficeDTO);
        }
        return serviceOfficeResponses;
    }
}
