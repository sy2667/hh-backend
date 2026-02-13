package com.household.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "hh_user_refresh_token")
@Getter
@Setter
public class UserRefreshToken {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_pk", nullable = false)
  private Integer userPk;

  @Column(name = "session_id", nullable = false, length = 36, unique = true)
  private String sessionId;

  @Column(name = "token_hash", nullable = false, length = 255)
  private String tokenHash;

  @Column(name = "issued_at", nullable = false)
  private LocalDateTime issuedAt;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false)
  private boolean revoked;

  @Column(name = "revoked_at")
  private LocalDateTime revokedAt;

  @Column(name = "user_agent", length = 255)
  private String userAgent;

  @Column(name = "ip_address", length = 45)
  private String ipAddress;
}
