package com.web.careus.model.ziswaf;

import com.web.careus.enumeration.InfakCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Infak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long infakId;
    private String infakCode;

    @Enumerated(EnumType.STRING)
    private InfakCategory infakCategory;
    private double amount;
    private double distribution;
}
