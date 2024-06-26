package com.web.careus.repository;

import com.web.careus.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);
    User findByUsername(String username);
    User findByVaNumber(long vaNumber);
    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String PhoneNumber);

    @Query(value = "SELECT COUNT(*) AS total_users FROM users WHERE role_id = 3", nativeQuery = true)
    long getTotalUser();

    @Query(value = "SELECT COUNT(*) AS total_users FROM users WHERE role_id != 1 AND YEAR(created_at) = :year", nativeQuery = true)
    long getTotalUserByYear(@Param("year") int year);

}
