package com.web.careus.model.user;

import com.web.careus.enumeration.EServiceOffice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ServiceOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceOfficeId;

    @Enumerated(EnumType.STRING)
    private EServiceOffice serviceOffice;
}
