package com.web.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.ziswaf.Infak;
import com.web.careus.model.ziswaf.Wakaf;
import com.web.careus.model.ziswaf.Zakat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long distributionId;

    private double distributionAmount;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate distributionDate;

    private String receiver;

    private String image;

    private String description;

    private boolean success;

    @Column(length = 10)
    private String category;

    @ManyToOne
    @JoinColumn(name = "zakat_id")
    private Zakat zakat;

    @ManyToOne
    @JoinColumn(name = "infak_id")
    private Infak infak;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "wakaf_id")
    private Wakaf wakaf;
}
