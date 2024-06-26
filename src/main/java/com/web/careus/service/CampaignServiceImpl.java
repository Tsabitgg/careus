package com.web.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.web.careus.dto.request.CampaignRequest;
import com.web.careus.dto.response.CampaignResponse;
import com.web.careus.enumeration.CampaignCategory;
import com.web.careus.enumeration.ERole;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.campaign.Category;
import com.web.careus.model.transaction.Submission;
import com.web.careus.model.user.User;
import com.ict.careus.repository.*;
import com.web.careus.repository.CampaignRepository;
import com.web.careus.repository.CategoryRepository;
import com.web.careus.repository.SubmissionRepository;
import com.web.careus.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CampaignServiceImpl implements CampaignService{

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    @Transactional
    public Campaign createCampaign(CampaignRequest campaignRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN) && !existingUser.getRole().getName().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            Category category = categoryRepository.findById(campaignRequest.getCategoryId()).orElseThrow(() -> new BadRequestException("Category not found"));
            Campaign campaign = modelMapper.map(campaignRequest, Campaign.class);
            campaign.setCategory(category);
            campaign.setCurrentAmount(0);
            campaign.setDistribution(0);

            if (campaignRepository.findByCampaignCode(campaignRequest.getCampaignCode()) != null) {
                throw new BadRequestException("Error: campaignCode is already taken!");
            }

            if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            campaignRequest.getCampaignImage().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    campaign.setCampaignImage(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String baseurl = "www.lazismu.com/campaign/";
            campaign.setGenerateLink(baseurl + campaign.getCampaignCode());

            if (existingUser.getRole().getName().equals(ERole.ADMIN)){
                campaign.setApproved(true);
            } else if (existingUser.getRole().getName().equals(ERole.SUB_ADMIN)){
                campaign.setApproved(false);
            }
            campaign.setCreator(existingUser);

            return campaignRepository.save(campaign);
        }
        throw new BadRequestException("Admin not found");
    }


    @Override
    @Transactional
    public Campaign updateCampaign(String campaignCode, CampaignRequest campaignRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Pastikan pengguna memiliki peran "ADMIN"
            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can update campaigns");
            }

            Campaign updateCampaign = campaignRepository.findByCampaignCode(campaignCode);

            if (updateCampaign != null) {
                if (!campaignCode.equals(campaignRequest.getCampaignCode()) &&
                        campaignRepository.findByCampaignCode(campaignRequest.getCampaignCode()) != null) {
                    throw new BadRequestException("campaignCode is already taken!");
                }

                updateCampaign.setCampaignCode(campaignRequest.getCampaignCode());
                updateCampaign.setCampaignName(campaignRequest.getCampaignName());
                updateCampaign.setDescription(campaignRequest.getDescription());
                updateCampaign.setLocation(campaignRequest.getLocation());
                updateCampaign.setTargetAmount(campaignRequest.getTargetAmount());
                updateCampaign.setCurrentAmount(campaignRequest.getCurrentAmount());
                updateCampaign.setActive(campaignRequest.isActive());

                if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
                    try {
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                                campaignRequest.getCampaignImage().getBytes(),
                                ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("url").toString();
                        updateCampaign.setCampaignImage(imageUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String baseUrl = "www.lazismu.com/campaign/";
                updateCampaign.setGenerateLink(baseUrl + updateCampaign.getCampaignCode());

                return campaignRepository.save(updateCampaign);
            } else {
                throw new BadRequestException("Campaign not found!");
            }
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    @Transactional
    public void deleteCampaign(long campaignId) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can delete campaigns");
            }
            campaignRepository.deleteById(campaignId);
        }
    }

    @Override
    @Transactional
    public Campaign approveCampaign(String campaignCode) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can approve campaigns");
            }

            Campaign campaign = campaignRepository.findByCampaignCode(campaignCode);
            if (campaign == null){
                throw new BadRequestException("CampaignCode not found");
            }

            campaign.setApproved(true);

            return campaignRepository.save(campaign);
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Page<Campaign> getCampaignByActiveAndApproved(Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findCampaignByActiveAndApproved(pageable);
        for (Campaign campaign : campaigns.getContent()) {
            if (campaign.getCurrentAmount() >= campaign.getTargetAmount()) {
                campaign.setActive(false);
                campaignRepository.save(campaign);
            }
        }
        return campaigns;
    }

    @Override
    public Page<Campaign> getHistoryCampaign(Pageable pageable) {
        Page<Campaign> historyCampaigns = campaignRepository.findHistoryCampaign(pageable);
        return historyCampaigns;
    }


//    @Override
//    public List<Campaign> getApprovedCampaigns() {
//        return campaignRepository.findByApproved(true);
//    }
//
//    @Override
//    public List<Campaign> getCampaignActive(boolean isActive) {
//        return campaignRepository.findCampaignByActive(isActive);
//    }


    @Override
    public List<Campaign> getPendingCampaigns() {
        return campaignRepository.findByApproved(false);
    }

    @Override
    public Page<Campaign> getAllCampaign(Pageable pageable) {
        return campaignRepository.findAll(pageable);
    }

    @Override
    public Optional<Campaign> getCampaignByCode(String campaignCode) {
        return Optional.ofNullable(campaignRepository.findByCampaignCode(campaignCode));
    }

    @Override
    public Optional<Campaign> getCampaignById(long campaignId) {
        return Optional.ofNullable(campaignRepository.findById(campaignId));
    }

    @Override
    public Page<Campaign> getCampaignByName(String campaignName, Pageable pageable) {
        return campaignRepository.findByCampaignName(campaignName, pageable);
    }

    @Override
    public Page<Campaign> getCampaignByCategoryName(String categoryName, Pageable pageable) {
        return campaignRepository.findByCategoryName(CampaignCategory.valueOf(categoryName.toUpperCase()), pageable);
    }

    @Override
    public Page<Campaign> getCampaignsByYear(int year, Pageable pageable) {
        return campaignRepository.findByYear(year, pageable);
    }

    @Override
    public Page<Campaign> getCampaignsByServiceOffice(long serviceOfficeId,Pageable pageable) {
        return campaignRepository.findCampaignsByServiceOfficeId(serviceOfficeId, pageable);
    }

    @Override
    public Page<CampaignResponse> getDetailsCampaign(Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findAllByApprovedIsTrue(pageable);
        return campaigns.map(this::mapToCampaignResponse);
    }

    private CampaignResponse mapToCampaignResponse(Campaign campaign) {
        CampaignResponse response = new CampaignResponse();
        response.setActive(campaign.isActive());
        response.setApproved(campaign.isApproved());
        response.setCampaignCode(campaign.getCampaignCode());
        response.setCampaignName(campaign.getCampaignName());
        response.setCreator(campaign.getCreator().getUsername());
        response.setDescription(campaign.getDescription());
        response.setStartDate(campaign.getStartDate());
        response.setEndDate(campaign.getEndDate());
        response.setTargetAmount(campaign.getTargetAmount());
        response.setCurrentAmount(campaign.getCurrentAmount());
        response.setLocation(campaign.getLocation());
        response.setCategoryName(campaign.getCategory().getCategoryName().name());
        double pengajuan = 0;
        double realisasi = 0;
        double distribution = campaign.getDistribution();
        List<Submission> submissions = submissionRepository.findAllByCampaign(campaign);
        for (Submission submission : submissions) {
            if (submission.isApproved()) {
                realisasi += submission.getSubmissionAmount();
            } else {
                pengajuan += submission.getSubmissionAmount();
            }
        }
        response.setPengajuan(pengajuan);
        response.setRealisasi(realisasi);
        response.setDistribution(distribution);
        return response;
    }

}
