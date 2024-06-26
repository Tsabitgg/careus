package com.web.careus.model.ziswaf;

import com.web.careus.enumeration.WakafCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wakaf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long wakafId;
    private String wakafCode;

    @Enumerated(EnumType.STRING)
    private WakafCategory wakafCategory;
    private double amount;
    private double distribution;
}
