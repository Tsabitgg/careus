package com.web.careus.repository;

import com.web.careus.model.campaign.Campaign;
import com.web.careus.model.transaction.Transaction;
import com.web.careus.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Procedure
    void update_campaign_current_amount(@Param("campaignCode") String campaignCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_zakat_amount(@Param("zakatCode") String zakatCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_infak_amount(@Param("infakCode") String zainfakCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_wakaf_amount(@Param("wakafCode") String wakafCode, @Param("TransactionAmount") double transactionAmount);

    List<Transaction> findByUser(User user);

    Page<Transaction> findByCampaign(Campaign campaign, Pageable pageable);

    @Query(value = "SELECT SUM(transaction.transaction_amount) FROM transaction INNER JOIN\n" +
            "campaign ON transaction.campaign_id = campaign.campaign_id", nativeQuery = true)
    double totalDonationCampaign();

    @Query("SELECT t FROM Transaction t WHERE YEAR(t.transactionDate) = :year")
    Page<Transaction> findByYear(@Param("year") int year, Pageable pageable);

    @Query(value = "SELECT\n" +
            "    SUM(transaction_amount) AS total_donation,\n" +
            "    SUM(CASE WHEN category = 'wakaf' THEN transaction_amount ELSE 0 END) AS wakaf_total,\n" +
            "    SUM(CASE WHEN category = 'infak' THEN transaction_amount ELSE 0 END) AS infak_total,\n" +
            "    SUM(CASE WHEN category = 'campaign' THEN transaction_amount ELSE 0 END) AS campaign_total,\n" +
            "    SUM(CASE WHEN category = 'zakat' THEN transaction_amount ELSE 0 END) AS zakat_total\n" +
            "FROM\n" +
            "    transaction\n" +
            "WHERE user_id = :userId", nativeQuery = true)
    Map<String, Double> getUserTransactionSummary(@Param("userId") Long userId);

    @Query(value = "SELECT\n" +
            "    SUM(transaction_amount) AS total_donation,\n" +
            "    SUM(CASE WHEN category = 'wakaf' THEN transaction_amount ELSE 0 END) AS wakaf_total,\n" +
            "    SUM(CASE WHEN category = 'infak' THEN transaction_amount ELSE 0 END) AS infak_total,\n" +
            "    SUM(CASE WHEN category = 'campaign' THEN transaction_amount ELSE 0 END) AS campaign_total,\n" +
            "    SUM(CASE WHEN category = 'zakat' THEN transaction_amount ELSE 0 END) AS zakat_total\n" +
            "FROM\n" +
            "    transaction\n" +
            "WHERE user_id = :userId AND YEAR(transaction_date) = :year", nativeQuery = true)
    Map<String, Double> getUserTransactionSummaryByYear(@Param("userId") Long userId, @Param("year") int year);

    @Query(value = "SELECT SUM(transaction_amount)" +
            "FROM transaction " +
            "WHERE YEAR(transaction_date) = :year", nativeQuery = true)
    Double getTotalTransactionAmountByYear(@Param("year") int year);

    @Query(value = "SELECT SUM(transaction_amount) " +
            "FROM transaction", nativeQuery = true)
    Double totalTransactionAmount();

    @Query(value = "SELECT count(transaction_amount) FROM transaction", nativeQuery = true)
    Double totalTransactionCount();

    @Query("SELECT t FROM Transaction t where t.category = 'zakat'")
    Page<Transaction> getZakatTransaction(Pageable pageable);

    @Query("SELECT t FROM Transaction t where t.category = 'infak'")
    Page<Transaction> getInfakTransaction(Pageable pageable);

    @Query("SELECT t FROM Transaction t where t.category = 'wakaf'")
    Page<Transaction> getWakafTransaction(Pageable pageable);

    Optional<Transaction> findByVaNumberAndRefNoAndTransactionDate(long vaNumber, String refNo, LocalDate transactionDate);
}
