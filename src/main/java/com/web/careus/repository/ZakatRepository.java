package com.web.careus.repository;

import com.web.careus.model.ziswaf.Zakat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZakatRepository extends JpaRepository<Zakat, Long> {
    Zakat findByZakatCode(String zakatCode);

    @Query("SELECT\n" +
            " z.zakatId,\n" +
            " z.zakatCategory,\n" +
            " z.zakatCode,\n" +
            " z.amount,\n" +
            " z.amount * 0.15 AS amil\n" +
            "FROM\n" +
            " Zakat z\n" +
            "GROUP BY z.zakatId, z.zakatCategory")
    Page<Object []> getAmilZakat(Pageable pageable);

}
