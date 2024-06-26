package com.web.careus.dto.response;

import com.web.careus.enumeration.InfakCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmilInfakResponse {
    private long infakId;
    private InfakCategory infakCategory;
    private String infakCode;
    private double amount;
    private double amil;
}
