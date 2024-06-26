package com.web.careus.repository;

import com.web.careus.model.ziswaf.Infak;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InfakRepository extends JpaRepository<Infak, Long> {
    Infak findByInfakCode(String infakCode);

    @Query("SELECT\n" +
            " i.infakId,\n" +
            " i.infakCategory,\n" +
            " i.infakCode,\n" +
            " i.amount,\n" +
            " i.amount * 0.15 AS amil\n" +
            "FROM\n" +
            " Infak i\n" +
            "GROUP BY i.infakId, i.infakCategory")
    Page<Object []> getAmilInfak(Pageable pageable);
}
