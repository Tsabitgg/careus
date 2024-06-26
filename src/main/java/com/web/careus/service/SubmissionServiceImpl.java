package com.web.careus.service;

import com.web.careus.enumeration.ERole;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Submission;
import com.web.careus.model.user.User;
import com.web.careus.repository.CampaignRepository;
import com.web.careus.repository.SubmissionRepository;
import com.web.careus.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public List<Submission> getAllSubmission() {
        return submissionRepository.findAll();
    }

    @Override
    public Submission createSubmission(Submission submissionRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Fetch the Campaign based on the provided campaignId
            Campaign campaign = campaignRepository.findById(submissionRequest.getCampaign().getCampaignId())
                    .orElseThrow(() -> new BadRequestException("Campaign not found"));

            // Create a new Submission
            Submission submission = new Submission();
            submission.setUser(existingUser);
            submission.setCampaign(campaign);
            submission.setSubmissionAmount(submissionRequest.getSubmissionAmount());
            submission.setSubmissionDate(new Date());
            submission.setApproved(false);

            // Save the submission
            return submissionRepository.save(submission);
        } else {
            throw new BadRequestException("User not authenticated");
        }
    }



    @Override
    public void deleteSubmission(long submissionId) {
        submissionRepository.deleteById(submissionId);
    }

    @Override
    public Submission approveSubmission(Long submissionId) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new BadRequestException("User not found");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
            throw new BadRequestException("Only ADMIN users can approve submission");
        }

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new BadRequestException("Submission not found"));

        submission.setApproved(true);
        submission.setApprovedDate(new Date());

        submissionRepository.save(submission);

        return submission;
    }


    @Override
    public List<Submission> getApprovedSubmission() {
        return submissionRepository.findByApproved(true);
    }

    @Override
    public List<Submission> getPendingSubmission() {
        return submissionRepository.findByApproved(false);
    }

    @Override
    public Page<Submission> getApprovedSubmissionBySubAdminId(long userId, Pageable pageable) {
        return submissionRepository.findApprovedSubmissionBySubAdminId(userId, pageable);
    }
}