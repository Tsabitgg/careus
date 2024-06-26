package com.web.careus.service;

import com.web.careus.dto.request.CampaignRequest;
import com.web.careus.dto.response.CampaignResponse;
import com.web.careus.model.campaign.Campaign;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    Campaign createCampaign(CampaignRequest campaignRequest) throws BadRequestException;
    Campaign updateCampaign(String campaignCode, CampaignRequest campaignRequest) throws BadRequestException;

    Campaign approveCampaign(String campaignCode) throws BadRequestException;

//    List<Campaign> getApprovedCampaigns();
//    List<Campaign> getCampaignActive(boolean isActive);

    Page<Campaign> getCampaignByActiveAndApproved(Pageable pageable);
    Page<Campaign> getHistoryCampaign(Pageable pageable);

    List<Campaign> getPendingCampaigns();

    void deleteCampaign(long campaignId) throws BadRequestException;
    Page<Campaign> getAllCampaign(Pageable pageable);
    Optional<Campaign> getCampaignByCode(String campaignCode);
    Optional<Campaign> getCampaignById(long campaignId);
    Page<Campaign> getCampaignByName(String campaignName, Pageable pageable);
    Page<Campaign> getCampaignByCategoryName(String categoryName, Pageable pageable);
    Page<Campaign> getCampaignsByYear(int year, Pageable pageable);

    Page<Campaign> getCampaignsByServiceOffice(long serviceOfficeId, Pageable pageable);
    Page<CampaignResponse> getDetailsCampaign(Pageable pageable);
}
