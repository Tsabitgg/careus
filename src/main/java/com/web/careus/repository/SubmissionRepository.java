package com.web.careus.repository;

import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByApproved(boolean approved);
    List<Submission> findAllByCampaign(Campaign campaign);

    @Query("SELECT s FROM Submission s WHERE s.user.userId = :userId AND s.approved = true")
    Page<Submission> findApprovedSubmissionBySubAdminId(@Param("userId") long userId, Pageable pageable);
}
