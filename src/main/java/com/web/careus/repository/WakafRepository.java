package com.web.careus.repository;

import com.web.careus.model.ziswaf.Wakaf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WakafRepository extends JpaRepository<Wakaf, Long> {
    Wakaf findByWakafCode(String wakafCode);

    @Query("SELECT\n" +
            " w.wakafId,\n" +
            " w.wakafCategory,\n" +
            " w.wakafCode,\n" +
            " w.amount,\n" +
            " w.amount * 0.15 AS amil\n" +
            "FROM\n" +
            " Wakaf w\n" +
            "GROUP BY w.wakafId, w.wakafCategory")
    Page<Object []> getAmilWakaf(Pageable pageable);
}
