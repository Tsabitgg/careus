package com.web.careus.service;

import com.web.careus.dto.request.TransactionRequest;
import com.web.careus.dto.response.CampaignTransactionsHistoryResponse;
import com.web.careus.dto.response.UserTransactionsHistoryResponse;
import com.web.careus.enumeration.ERole;
import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Transaction;
import com.web.careus.model.user.Role;
import com.web.careus.model.user.User;
import com.web.careus.model.ziswaf.Infak;
import com.web.careus.model.ziswaf.Wakaf;
import com.web.careus.model.ziswaf.Zakat;
import com.web.careus.repository.*;
import com.web.careus.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException {
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
            user = userRepository.save(user);
        } else {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            user = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
        }

        Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
        transaction.setUser(user);
        if (transactionRequest.getUsername() == null){
            transaction.setUsername(user.getUsername());
        } else if (transactionRequest.getUsername() != null){
            transaction.setUsername(transaction.getUsername());
        }
        transaction.setPhoneNumber(user.getPhoneNumber());

        switch (transactionType) {
            case "campaign":
                Campaign campaign = campaignRepository.findByCampaignCode(code);
                if (campaign != null) {
                    transaction.setCampaign(campaign);
                } else {
                    throw new RuntimeException("Campaign not found with code: " + code);
                }
                break;
            case "zakat":
                Zakat zakat = zakatRepository.findByZakatCode(code);
                if (zakat != null) {
                    transaction.setZakat(zakat);
                } else {
                    throw new RuntimeException("Zakat not found with code: " + code);
                }
                break;
            case "infak":
                Infak infak = infakRepository.findByInfakCode(code);
                if (infak != null) {
                    transaction.setInfak(infak);
                } else {
                    throw new RuntimeException("Infak not found with code: " + code);
                }
                break;
            case "wakaf":
                Wakaf wakaf = wakafRepository.findByWakafCode(code);
                if (wakaf != null) {
                    transaction.setWakaf(wakaf);
                } else {
                    throw new RuntimeException("Wakaf not found with code: " + code);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }

        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCategory(transactionType);
        transaction.setMethod("OFFLINE");
        transaction.setSuccess(true);

        transaction = transactionRepository.save(transaction);

    // Update jumlah transaksi terkait berdasarkan tipe transaksi
        switch (transactionType) {
            case "campaign":
                transactionRepository.update_campaign_current_amount(code, transaction.getTransactionAmount());
                break;
            case "zakat":
                transactionRepository.update_zakat_amount(code, transaction.getTransactionAmount());
                break;
            case "infak":
                transactionRepository.update_infak_amount(code, transaction.getTransactionAmount());
                break;
            case "wakaf":
                transactionRepository.update_wakaf_amount(code, transaction.getTransactionAmount());
                break;
        }

        return transaction;
    }

    @Override
    public Page<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign, Pageable pageable) {
        Page<Transaction> campaignTransactions = transactionRepository.findByCampaign(campaign, pageable);
        return campaignTransactions.map(this::campaignTransactionsDTO);
    }

    @Override
    public Page<Transaction> getAllTransaction(int year, Pageable pageable) {
        return transactionRepository.findByYear(year, pageable);
    }


    @Override
    public double getTotalTransactionCount() {
        return transactionRepository.totalTransactionCount();
    }

    @Override
    public double getTotalDonationCampaign(){
        return transactionRepository.totalDonationCampaign();
    }

    @Override
    public Map<String, Double> getUserTransactionSummary() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            long userId = existingUser.getUserId();
            return transactionRepository.getUserTransactionSummary(userId);
        }
        throw new BadRequestException("User not found");
    }


    @Override
    public Map<String, Double> getUserTransactionSummaryByYear(int year) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            long userId = existingUser.getUserId();
            return transactionRepository.getUserTransactionSummaryByYear(userId, year);
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Optional<Transaction> getTransactionById(long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public Page<Transaction> getZakatTransaction(Pageable pageable) {
        return transactionRepository.getZakatTransaction(pageable);
    }

    @Override
    public Page<Transaction> getInfakTransaction(Pageable pageable) {
        return transactionRepository.getInfakTransaction(pageable);
    }

    @Override
    public Page<Transaction> getWakafTransaction(Pageable pageable) {
        return transactionRepository.getWakafTransaction(pageable);
    }

    private CampaignTransactionsHistoryResponse campaignTransactionsDTO(Transaction transaction) {
        CampaignTransactionsHistoryResponse campaignTransactionsDTO = new CampaignTransactionsHistoryResponse();
        campaignTransactionsDTO.setUsername(transaction.getUsername());
        campaignTransactionsDTO.setTransactionAmount(transaction.getTransactionAmount());
        campaignTransactionsDTO.setMessage(transaction.getMessage());
        campaignTransactionsDTO.setTransactionDate(transaction.getTransactionDate());

        return campaignTransactionsDTO;
    }

    @Override
    public List<UserTransactionsHistoryResponse> getUserTransactionsHistory() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            List<Transaction> userTransactions = transactionRepository.findByUser(existingUser);
            return userTransacctionsDTO(userTransactions);
        }
        throw new BadRequestException("User not found");
    }

    private List<UserTransactionsHistoryResponse> userTransacctionsDTO(List<Transaction> transactions) {
        List<UserTransactionsHistoryResponse> userTransactionsHistory = new ArrayList<>();
        for (Transaction transaction : transactions) {
            UserTransactionsHistoryResponse transactionDTO = new UserTransactionsHistoryResponse();
            transactionDTO.setUsername(transaction.getUsername());
            transactionDTO.setTransactionAmount(transaction.getTransactionAmount());
            transactionDTO.setMessage(transaction.getMessage());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setSuccess(transaction.isSuccess());

            if (transaction.getCampaign() != null) {
                transactionDTO.setType("Campaign");
                transactionDTO.setTransactionName(transaction.getCampaign().getCampaignName());
            } else if (transaction.getZakat() != null) {
                transactionDTO.setType("Zakat");
                transactionDTO.setTransactionName(transaction.getZakat().getZakatCategory().name());
            } else if (transaction.getInfak() != null) {
                transactionDTO.setType("Infak");
                transactionDTO.setTransactionName(transaction.getInfak().getInfakCategory().name());
            } else if (transaction.getWakaf() != null) {
                transactionDTO.setType("Wakaf");
                transactionDTO.setTransactionName(transaction.getWakaf().getWakafCategory().name());
            }

            userTransactionsHistory.add(transactionDTO);
        }
        return userTransactionsHistory;
    }

}

