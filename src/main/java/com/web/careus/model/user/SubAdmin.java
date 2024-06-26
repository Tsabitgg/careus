package com.web.careus.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubAdmin extends User {

    @ManyToOne
    @JoinColumn(name = "service_office_id",referencedColumnName = "serviceOfficeid")
    private ServiceOffice serviceOffice;

    public SubAdmin(String username, String phoneNumber, String password, String address, Role role, ServiceOffice serviceOffice) {
        super(username, phoneNumber, password, address, role);
        this.serviceOffice = serviceOffice;
    }
}
