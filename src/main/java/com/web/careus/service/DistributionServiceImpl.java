package com.web.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.web.careus.dto.request.DistributionRequest;
import com.web.careus.dto.response.CampaignDistributionHistoryResponse;
import com.web.careus.enumeration.ERole;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Distribution;
import com.web.careus.model.user.User;
import com.web.careus.model.ziswaf.Infak;
import com.web.careus.model.ziswaf.Wakaf;
import com.web.careus.model.ziswaf.Zakat;
import com.web.careus.repository.*;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class DistributionServiceImpl implements DistributionService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Distribution createDistribution(String distributionType, String code, DistributionRequest distributionRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN) && !existingUser.getRole().getName().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN and SUB ADMIN can Distribute");
            }

            Distribution distribution = modelMapper.map(distributionRequest, Distribution.class);

            switch (distributionType) {
                case "campaign":
                    Campaign campaign = campaignRepository.findByCampaignCode(code);
                    if (campaign != null) {
                        distribution.setCampaign(campaign);
                        distributionRepository.update_campaign_distribution(code, distribution.getDistributionAmount());
                    } else {
                        throw new RuntimeException("Campaign not found with code: " + code);
                    }
                    break;
                case "zakat":
                    Zakat zakat = zakatRepository.findByZakatCode(code);
                    if (zakat != null) {
                        distribution.setZakat(zakat);
                        distributionRepository.update_zakat_distribution(code, distribution.getDistributionAmount());
                    } else {
                        throw new RuntimeException("Zakat not found with code: " + code);
                    }
                    break;
                case "infak":
                    Infak infak = infakRepository.findByInfakCode(code);
                    if (infak != null) {
                        distribution.setInfak(infak);
                        distributionRepository.update_infak_distribution(code, distribution.getDistributionAmount());
                    } else {
                        throw new RuntimeException("Infak not found with code: " + code);
                    }
                    break;
                case "wakaf":
                    Wakaf wakaf = wakafRepository.findByWakafCode(code);
                    if (wakaf != null) {
                        distribution.setWakaf(wakaf);
                        distributionRepository.update_wakaf_distribution(code, distribution.getDistributionAmount());
                    } else {
                        throw new RuntimeException("Wakaf not found with code: " + code);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid distribution distributionType: " + distributionType);
            }

            if (distributionRequest.getImage() != null && !distributionRequest.getImage().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            distributionRequest.getImage().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    distribution.setImage(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image.", e);
                }
            }
            distribution.setCategory(distributionType);

            return distributionRepository.save(distribution);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public Page<CampaignDistributionHistoryResponse> getCampaignDistributionHistory(Campaign campaign, Pageable pageable) {
        Page<Distribution> campaignDistribution = distributionRepository.findByCampaign(campaign, pageable);
        return campaignDistribution.map(this::campaignDistributionDTO);
    }

    @Override
    public Page<Distribution> getAllDistribution(Pageable pageable) {
        return distributionRepository.findAll(pageable);
    }

    private CampaignDistributionHistoryResponse campaignDistributionDTO(Distribution distribution) {
        CampaignDistributionHistoryResponse campaignDistributionDTO = new CampaignDistributionHistoryResponse();
        campaignDistributionDTO.setId(distribution.getDistributionId());
        campaignDistributionDTO.setDistributionAmount(distribution.getDistributionAmount());
        campaignDistributionDTO.setReceiver(distribution.getReceiver());
        campaignDistributionDTO.setDistributionDate(distribution.getDistributionDate());
        campaignDistributionDTO.setImage(distribution.getImage());
        campaignDistributionDTO.setDescription(distribution.getDescription());

        return campaignDistributionDTO;
    }
}