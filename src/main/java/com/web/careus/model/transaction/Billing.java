package com.web.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.user.User;
import com.web.careus.model.ziswaf.Infak;
import com.web.careus.model.ziswaf.Wakaf;
import com.web.careus.model.ziswaf.Zakat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long billingId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(length = 20)
    private String username;

    @Column(length = 15)
    private String phoneNumber;

    private double billingAmount;
    private String message;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime billingDate;

    private long vaNumber;
    private String method;
    private boolean success;

    @Column(length = 20)
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
