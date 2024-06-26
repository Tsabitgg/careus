package com.web.careus.service;

import com.web.careus.model.transaction.Submission;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubmissionService {
    List<Submission> getAllSubmission();
    Submission createSubmission(Submission submission) throws BadRequestException;
    void deleteSubmission(long submissionId);

    Submission approveSubmission(Long submissionId) throws BadRequestException;
    List<Submission> getApprovedSubmission();
    List<Submission> getPendingSubmission();

    Page<Submission> getApprovedSubmissionBySubAdminId(long userId, Pageable pageable);
}
