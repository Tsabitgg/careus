package com.web.careus.repository;

import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Procedure
    void update_campaign_distribution(@Param("campaignCode") String campaigCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_zakat_distribution(@Param("zakatCode") String zakatCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_infak_distribution(@Param("infakCode") String infakCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_wakaf_distribution(@Param("wakafCode") String wakafCode, @Param("distributionAmount") double distributionAmount);

    @Query(value = "SELECT SUM(distribution_amount) AS Total_distribusi FROM distribution", nativeQuery = true)
    Double totalDistributionAmount();

    @Query(value = "SELECT SUM(distribution_amount) AS Total_distribusi FROM distribution " +
            "WHERE YEAR(distribution_date) = :year", nativeQuery = true)
    Double totalDistributionAmountByYear(@Param("year") int year);

    @Query(value = "SELECT COUNT(DISTINCT receiver) AS penerima_manfaat FROM distribution", nativeQuery = true)
    long totalDistributionReceiver();

    @Query(value = "SELECT COUNT(DISTINCT receiver) AS penerima_manfaat FROM distribution " +
            "WHERE YEAR(distribution_date) = :year", nativeQuery = true)
    long totalDistributionReceiverByYear(@Param("year") int year);

    Page<Distribution> findByCampaign(Campaign campaign, Pageable pageable);
}