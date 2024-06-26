package com.web.careus.model.campaign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.careus.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private String campaignName;

    private String campaignCode;
    private String campaignImage;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "userId")
    private User creator;
    private double distribution;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(columnDefinition = "BOOLEAN")
    private boolean active;

    private String generateLink;

    @Column(columnDefinition = "BOOLEAN")
    private boolean approved;
}
