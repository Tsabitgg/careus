package com.web.careus.model.ziswaf;

import com.web.careus.enumeration.ZakatCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Zakat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zakatId;
    private String zakatCode;

    @Enumerated(EnumType.STRING)
    private ZakatCategory zakatCategory;
    private double amount;
    private double distribution;
}
