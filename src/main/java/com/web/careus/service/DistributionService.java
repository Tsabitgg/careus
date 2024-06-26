package com.web.careus.service;

import com.web.careus.dto.request.DistributionRequest;
import com.web.careus.dto.response.CampaignDistributionHistoryResponse;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Distribution;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DistributionService {
    Distribution createDistribution(String type, String code, DistributionRequest distributionRequest) throws BadRequestException;
    Page<CampaignDistributionHistoryResponse> getCampaignDistributionHistory(Campaign campaign, Pageable pageable);
    Page<Distribution> getAllDistribution(Pageable pageable);
}
