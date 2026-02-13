package com.household.backend.repository;

import com.household.backend.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
  Optional<UserRefreshToken> findBySessionId(String sessionId);

  @Modifying
  @Query("""
    DELETE FROM UserRefreshToken token
       WHERE token.expiresAt < CURRENT_TIMESTAMP
          OR (token.revoked = true AND token.revokedAt < :threshold)
  """)
  int deleteExpiredAndOldRevoked(@Param("threshold")LocalDateTime threshold);
}
