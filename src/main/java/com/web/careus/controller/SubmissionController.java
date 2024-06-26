package com.web.careus.controller;

import com.web.careus.dto.response.MessageResponse;
import com.web.careus.model.transaction.Submission;
import com.web.careus.service.SubmissionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/submission")
    public ResponseEntity<List<Submission>> getAllSubmission(){
        List<Submission> submissions = submissionService.getAllSubmission();
        return ResponseEntity.ok(submissions);
    }

    @PostMapping("/submission/create")
    public ResponseEntity<?> createSubmission(@RequestBody Submission submissionRequest) {
        try {
            Submission createdSubmission = submissionService.createSubmission(submissionRequest);
            return ResponseEntity.ok().body(createdSubmission);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/submission/delete/{submissionId}")
    public MessageResponse deleteSubmission(@PathVariable long submissionId) throws BadRequestException {
        submissionService.deleteSubmission(submissionId);
        return new MessageResponse("Delete Submission successfully");
    }

    @PutMapping("/admin/submission/approve-submission")
    public ResponseEntity<?> approveSubmission(@RequestParam long submissionId) {
        try {
            Submission approvedSubmission = submissionService.approveSubmission(submissionId);
            return new ResponseEntity<>(approvedSubmission, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/submission/approved")
    public ResponseEntity<List<Submission>> getApprovedSubmission() {
        List<Submission> approvedSubmission = submissionService.getApprovedSubmission();
        return new ResponseEntity<>(approvedSubmission, HttpStatus.OK);
    }

    @GetMapping("/submission/pending")
    public ResponseEntity<List<Submission>> getPendingSubmission() {
        List<Submission> pendingSubmission = submissionService.getPendingSubmission();
        return new ResponseEntity<>(pendingSubmission, HttpStatus.OK);
    }

    @GetMapping("/submission/get-approved-by-subAdmin/{userId}")
    public ResponseEntity<Page<Submission>> getApprovedSubmissionBySubAdmin(@PathVariable long userId,
                                                                            @RequestParam(value = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Submission> submissions = submissionService.getApprovedSubmissionBySubAdminId(userId, pageRequest);
        return ResponseEntity.ok(submissions);
    }
}
