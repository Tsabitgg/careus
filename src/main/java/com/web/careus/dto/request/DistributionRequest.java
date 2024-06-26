package com.web.careus.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DistributionRequest {
    private double distributionAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate distributionDate;

    private String receiver;

    private MultipartFile image;

    private String description;

    private boolean success;
}