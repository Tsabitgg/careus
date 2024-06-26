package com.web.careus.controller;

import com.web.careus.dto.request.CampaignRequest;
import com.web.careus.dto.response.CampaignDistributionHistoryResponse;
import com.web.careus.dto.response.CampaignResponse;
import com.web.careus.dto.response.CampaignTransactionsHistoryResponse;;
import com.web.careus.dto.response.MessageResponse;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.service.CampaignService;
import com.web.careus.service.DistributionService;
import com.web.careus.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DistributionService distributionService;

    @PostMapping("/admin/create-campaign")
    public ResponseEntity<?> createCampaign(@ModelAttribute CampaignRequest campaignRequest) {
        try {
            Campaign createdCampaign = campaignService.createCampaign(campaignRequest);
            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/admin/update-campaign/{campaignCode}")
    public ResponseEntity<?> updateCampaign(@PathVariable String campaignCode, @ModelAttribute CampaignRequest campaignRequest) {
        try{
            Campaign updatedCampaign = campaignService.updateCampaign(campaignCode, campaignRequest);
            return new ResponseEntity<>(updatedCampaign, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @CrossOrigin
    @PutMapping("/admin/approve-campaign")
    public ResponseEntity<?> approveCampaign(@RequestParam String campaignCode) {
        try {
            Campaign approvedCampaign = campaignService.approveCampaign(campaignCode);
            return new ResponseEntity<>(approvedCampaign, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/delete-campaign/{campaignId}")
    public MessageResponse deleteCampaign(@PathVariable long campaignId) throws BadRequestException {
        campaignService.deleteCampaign(campaignId);
        return new MessageResponse("Delete campaign successfully");
    }

    @GetMapping("/campaign")
    public Page<Campaign> getAllCampaigns(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (year != null) {
            return campaignService.getCampaignsByYear(year, pageRequest);
        } else {
            return campaignService.getAllCampaign(pageRequest);
        }
    }

//    @GetMapping("/campaign/approved")
//    public ResponseEntity<List<Campaign>> getApprovedCampaigns() {
//        List<Campaign> approvedCampaigns = campaignService.getApprovedCampaigns();
//        return new ResponseEntity<>(approvedCampaigns, HttpStatus.OK);
//    }
//
//    @GetMapping("/campaign/active")
//    public ResponseEntity<List<Campaign>> getCampaignActive(){
//        List<Campaign> activeCampaign = campaignService.getCampaignActive(true);
//        return ResponseEntity.ok(activeCampaign);
//    }

    @GetMapping("/campaign/active-and-approved-campaign")
    public Page<Campaign> getCampaignByActiveAndApproved(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getCampaignByActiveAndApproved(pageRequest);
    }

    @GetMapping("/campaign/history-campaign")
    public Page<Campaign> getHistoryCampaign(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getHistoryCampaign(pageRequest);
    }

    @GetMapping("/campaign/pending")
    public ResponseEntity<List<Campaign>> getPendingCampaigns() {
        List<Campaign> pendingCampaigns = campaignService.getPendingCampaigns();
        return new ResponseEntity<>(pendingCampaigns, HttpStatus.OK);
    }


    @GetMapping("/campaign/{campaignCode}")
    public ResponseEntity<Campaign> getCampaignByCode(@PathVariable String campaignCode) {
        Optional<Campaign> campaignOptional = campaignService.getCampaignByCode(campaignCode);
        if (campaignOptional.isPresent()) {
            return new ResponseEntity<>(campaignOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/campaign/id")
    public ResponseEntity<Campaign> getCampaignById(@RequestParam long campaignId) {
        Optional<Campaign> campaignOptional = campaignService.getCampaignById(campaignId);
        if (campaignOptional.isPresent()){
            return new ResponseEntity<>(campaignOptional.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/campaign/search")
    public Page<Campaign> getCampaignByName(@RequestParam String campaignName,
                                            @RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        return campaignService.getCampaignByName(campaignName, pageRequest);
    }

    @GetMapping("/campaign/category")
    public Page<Campaign> getCampaigByCategoryName(@RequestParam String categoryName,
                                                   @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getCampaignByCategoryName(categoryName, pageRequest);
    }

    @GetMapping("/campaign/{campaignCode}/history")
    public ResponseEntity<Page<CampaignTransactionsHistoryResponse>> getCampaignTransactionsHistory(@PathVariable String campaignCode,
                                                                                                    @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 6;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Campaign campaign = campaignService.getCampaignByCode(campaignCode)
                .orElse(null);

        if (campaign == null) {
            return ResponseEntity.notFound().build();
        }

        Page<CampaignTransactionsHistoryResponse> campaignTransactionsPage = transactionService.getCampaignTransactionsHistory(campaign, pageRequest);
        return ResponseEntity.ok(campaignTransactionsPage);
    }

    @GetMapping("/campaign/{campaignCode}/distribution")
    public ResponseEntity<Page<CampaignDistributionHistoryResponse>> getCampaignDistributionHistory(@PathVariable String campaignCode,
                                                                                                    @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 6;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Campaign campaign = campaignService.getCampaignByCode(campaignCode)
                .orElse(null);

        if (campaign == null) {
            return ResponseEntity.notFound().build();
        }

        Page<CampaignDistributionHistoryResponse> campaignDistributionPage = distributionService.getCampaignDistributionHistory(campaign, pageRequest);
        return ResponseEntity.ok(campaignDistributionPage);
    }

    @GetMapping("/campaign/total-donation")
    public double getTotalDonationCampaign(){
        return transactionService.getTotalDonationCampaign();
    }

    @GetMapping("/campaign/get-by-service-office/{serviceOfficeId}")
    public Page<Campaign> getCampaignsByServiceOffice(@PathVariable long serviceOfficeId,
                                                      @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 6;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getCampaignsByServiceOffice(serviceOfficeId, pageRequest);
    }

    @GetMapping("/campaign/all-details")
    public Page<CampaignResponse> getDetailsCampaign(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getDetailsCampaign(pageRequest);
    }
}
