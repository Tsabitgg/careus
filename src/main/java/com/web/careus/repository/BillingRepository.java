package com.web.careus.repository;

import com.web.careus.model.transaction.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    @Query("SELECT b.success FROM Billing b WHERE b.billingId = :billingId")
    Optional<Boolean> findSuccessByBillingId(@Param("billingId") Long billingId);

}
