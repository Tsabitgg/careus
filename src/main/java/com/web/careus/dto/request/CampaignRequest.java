package com.web.careus.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CampaignRequest {

    private long campaignId;
    private long categoryId;
    private String campaignName;
    private String campaignCode;
    private MultipartFile campaignImage;
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(columnDefinition = "BOOLEAN")
    private boolean active;
}
