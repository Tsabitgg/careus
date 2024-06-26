package com.web.careus.service;

import com.web.careus.dto.request.TransactionRequest;
import com.web.careus.enumeration.ERole;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Billing;
import com.web.careus.model.user.Role;
import com.web.careus.model.user.User;
import com.web.careus.model.ziswaf.Infak;
import com.web.careus.model.ziswaf.Wakaf;
import com.web.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import com.web.careus.repository.*;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Billing createBilling(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;

        // Jika tidak ada autentikasi atau autentikasi bukan dari UserDetailsImpl, maka buat pengguna baru
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            // Validasi username dan phoneNumber
            if (transactionRequest.getUsername() == null || transactionRequest.getPhoneNumber() == null) {
                throw new BadRequestException("Username and phoneNumber cannot be null for new user");
            }

            user = new User();
            user.setUsername(transactionRequest.getUsername());
            user.setPhoneNumber(transactionRequest.getPhoneNumber());
            String password = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(userRole);
            user.setCreatedAt(new Date());
            Random random = new Random();
            long min = 1000000000L;
            long max = 9999999999L;

            for (int i = 0; i < 10; i++) {
                long vaNumber = min + (long) (random.nextDouble() * (max - min));
                user.setVaNumber(vaNumber);
            }
            user = userRepository.save(user);
        } else {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            user = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
        }

        Billing billing = modelMapper.map(transactionRequest, Billing.class);
        billing.setUser(user);
        if (transactionRequest.getUsername() == null) {
            billing.setUsername(user.getUsername());
        } else if (transactionRequest.getUsername() != null) {
            billing.setUsername(billing.getUsername());
        }
        billing.setPhoneNumber(user.getPhoneNumber());
        billing.setVaNumber(user.getVaNumber());

        switch (transactionType) {
            case "campaign":
                Campaign campaign = campaignRepository.findByCampaignCode(code);
                if (campaign != null) {
                    billing.setCampaign(campaign);
                } else {
                    throw new RuntimeException("Campaign not found with code: " + code);
                }
                break;
            case "zakat":
                Zakat zakat = zakatRepository.findByZakatCode(code);
                if (zakat != null) {
                    billing.setZakat(zakat);
                } else {
                    throw new RuntimeException("Zakat not found with code: " + code);
                }
                break;
            case "infak":
                Infak infak = infakRepository.findByInfakCode(code);
                if (infak != null) {
                    billing.setInfak(infak);
                } else {
                    throw new RuntimeException("Infak not found with code: " + code);
                }
                break;
            case "wakaf":
                Wakaf wakaf = wakafRepository.findByWakafCode(code);
                if (wakaf != null) {
                    billing.setWakaf(wakaf);
                } else {
                    throw new RuntimeException("Wakaf not found with code: " + code);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid billing type: " + transactionType);
        }

        billing.setBillingAmount(transactionRequest.getAmount());
        billing.setBillingDate(LocalDateTime.now());
        billing.setCategory(transactionType);
        billing.setMethod("VA NUMBER");
        billing.setSuccess(false);

        billing = billingRepository.save(billing);
        return billing;
    }

    @Override
    public boolean getBillingSuccess(Long billingId) {
        Optional<Boolean> success = billingRepository.findSuccessByBillingId(billingId);
        return success.orElse(false);
    }
}
