package com.web.careus.dto.response;

import com.web.careus.enumeration.ZakatCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmilZakatResponse {
    private long zakatId;
    private ZakatCategory zakatCategory;
    private String zakatCode;
    private double amount;
    private double amil;
}