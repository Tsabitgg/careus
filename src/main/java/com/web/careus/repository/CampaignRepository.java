package com.web.careus.repository;

import com.web.careus.enumeration.CampaignCategory;
import com.web.careus.model.campaign.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.category.categoryName = :categoryName " +
            "AND c.active = true " +
            "AND c.approved = true")
    Page<Campaign> findByCategoryName(@Param("categoryName") CampaignCategory categoryName, Pageable pageable);


    List<Campaign> findByApproved(boolean approved);
//    List<Campaign> findCampaignByActive(boolean isActive);

    @Query("SELECT c FROM Campaign c WHERE c.active = true AND c.approved = true")
    Page<Campaign> findCampaignByActiveAndApproved(Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE c.active = false AND c.approved = true")
    Page<Campaign> findHistoryCampaign(Pageable pageable);

    Campaign findByCampaignCode(String campaignCode);

    Campaign findById(long campaignId);

    @Query("SELECT c FROM Campaign c WHERE LOWER(c.campaignName) LIKE LOWER(CONCAT('%', :campaignName, '%'))")
    Page<Campaign> findByCampaignName(String campaignName, Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE YEAR(c.startDate) = :year")
    Page<Campaign> findByYear(@Param("year") int year, Pageable pageable);

    @Query("SELECT \n" +
            " c.campaignId,\n" +
            " c.campaignName,\n" +
            " c.location,\n" +
            " c.targetAmount,\n" +
            " c.currentAmount,\n" +
            " c.currentAmount * 0.15 AS amil,\n" +
            " c.active\n" +
            "FROM \n" +
            " Campaign c\n" +
            "GROUP BY c.campaignId, c.currentAmount")
    Page<Object []> getAmilCampaign(Pageable pageable);

    @Query("SELECT SUM(c.currentAmount) AS totalCampaignTransactionAmount, " +
            "SUM(c.currentAmount * 0.15) AS totalAmil, " +
            "SUM(c.distribution) AS totalCampaignDistributionAmount " +
            "FROM Campaign c")
    Optional<Map<String, Double>> getSummaryCampaign();

    @Query("SELECT c FROM Campaign c WHERE c.creator IN (SELECT sa FROM SubAdmin sa WHERE sa.serviceOffice.serviceOfficeId = :serviceOfficeId)")
    Page<Campaign> findCampaignsByServiceOfficeId(@Param("serviceOfficeId") long serviceOfficeId, Pageable pageable);

    Page<Campaign> findAllByApprovedIsTrue(Pageable pageable);



}
