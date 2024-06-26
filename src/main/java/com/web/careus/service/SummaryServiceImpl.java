package com.web.careus.service;

import com.web.careus.enumeration.InfakCategory;
import com.web.careus.enumeration.WakafCategory;
import com.web.careus.enumeration.ZakatCategory;
import com.web.careus.dto.response.*;
import com.web.careus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Override
    public SummaryResponse getSummary(Integer year) {
        SummaryResponse summary = new SummaryResponse();
        if (year != null) {
            summary.setTotalDistributionAmount(distributionRepository.totalDistributionAmountByYear(year));
            summary.setTotalDistributionReceiver(distributionRepository.totalDistributionReceiverByYear(year));
            summary.setTotalTransactionAmount(transactionRepository.getTotalTransactionAmountByYear(year));
            summary.setTotalUser(userRepository.getTotalUserByYear(year));
        } else {
            summary.setTotalDistributionAmount(distributionRepository.totalDistributionAmount());
            summary.setTotalDistributionReceiver(distributionRepository.totalDistributionReceiver());
            summary.setTotalTransactionAmount(transactionRepository.totalTransactionAmount());
            summary.setTotalUser(userRepository.getTotalUser());
        }
        return summary;
    }

    @Override
    public Page<AmilCampaignResponse> getAmilCampaign(Pageable pageable) {
        Page<Object[]> results = campaignRepository.getAmilCampaign(pageable);
        return results.map(result -> new AmilCampaignResponse(
                (Long) result[0],
                (String) result[1],
                (String) result[2],
                (Double) result[3],
                (Double) result[4],
                (Double) result[5],
                (Boolean) result[6]
        ));
    }

    @Override
    public Page<AmilZakatResponse> getAmilZakat(Pageable pageable) {
        Page<Object[]> results = zakatRepository.getAmilZakat(pageable);
        return results.map(result -> new AmilZakatResponse(
                (Long) result[0],
                (ZakatCategory) result[1],
                (String) result[2],
                (Double) result[3],
                (Double) result[4]
        ));
    }

    @Override
    public Page<AmilInfakResponse> getAmilInfak(Pageable pageable) {
        Page<Object[]> results = infakRepository.getAmilInfak(pageable);
        return results.map(result -> new AmilInfakResponse(
                (Long) result[0],
                (InfakCategory) result[1],
                (String) result[2],
                (Double) result[3],
                (Double) result[4]
        ));
    }

    @Override
    public Page<AmilWakafResponse> getAmilWakaf(Pageable pageable) {
        Page<Object[]> results = wakafRepository.getAmilWakaf(pageable);
        return results.map(result -> new AmilWakafResponse(
                (Long) result[0],
                (WakafCategory) result[1],
                (String) result[2],
                (Double) result[3],
                (Double) result[4]
        ));
    }

    @Override
    public Optional<SummaryCampaignResponse> getSummaryCampaign() {
        Optional<Map<String, Double>> summaryMap = campaignRepository.getSummaryCampaign();
        return summaryMap.map(map -> new SummaryCampaignResponse(
                map.getOrDefault("totalCampaignTransactionAmount", 0.0),
                map.getOrDefault("totalAmil", 0.0),
                map.getOrDefault("totalCampaignDistributionAmount", 0.0)));
    }
}
