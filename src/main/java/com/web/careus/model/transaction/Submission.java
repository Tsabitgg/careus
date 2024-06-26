package com.web.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long submissionId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaignId")
    private Campaign campaign;

    private double submissionAmount;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date submissionDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approvedDate = null;

    private boolean approved;
}
