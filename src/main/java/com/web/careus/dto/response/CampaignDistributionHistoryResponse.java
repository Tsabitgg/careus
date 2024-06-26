package com.web.careus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CampaignDistributionHistoryResponse {
    private long id;
    private double distributionAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate distributionDate;
    private String receiver;
    private String image;
    private String description;
}
