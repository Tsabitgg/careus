package com.web.careus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CampaignResponse {
    private boolean active;
    private boolean approved;
    private String campaignCode;
    private String campaignName;
    private String creator;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private double targetAmount;
    private double currentAmount;
    private String location;
    private String categoryName;
    private double pengajuan;
    private double realisasi;
    private double distribution;
}

