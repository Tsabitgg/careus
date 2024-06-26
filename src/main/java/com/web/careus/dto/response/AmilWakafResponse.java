package com.web.careus.dto.response;

import com.web.careus.enumeration.WakafCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmilWakafResponse {
    private long wakafId;
    private WakafCategory wakafCategory;
    private String wakafCode;
    private double amount;
    private double amil;
}
