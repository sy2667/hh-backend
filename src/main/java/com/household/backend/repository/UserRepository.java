package com.household.backend.repository;

import com.household.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u From User u WHERE u.provider = :provider AND u.providerId = :providerId")
    Optional<User> findByProvider(@Param("provider") String provider, @Param("providerId") String providerId);

    @Query("SELECT u From User u Where u.email = :email")
    Optional<User> findByEmail(@Param(("email")) String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.provider = :provider AND u.providerId = :providerId")
    boolean existsProvider(@Param("provider") String provider, @Param("providerId") String providerId);
}

