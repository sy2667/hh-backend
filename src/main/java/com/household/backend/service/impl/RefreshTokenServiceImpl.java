package com.household.backend.service.impl;

import com.household.backend.auth.JwtTokenProvider;
import com.household.backend.common.TokenHash;
import com.household.backend.entity.UserRefreshToken;
import com.household.backend.repository.UserRefreshTokenRepository;
import com.household.backend.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private final UserRefreshTokenRepository repo;
  private final JwtTokenProvider jwtTokenProvider;

  public RefreshTokenServiceImpl(UserRefreshTokenRepository repo, JwtTokenProvider jwtTokenProvider) {
    this.repo = repo;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public String issueAndStore(Integer userPk, HttpServletRequest request) {
    String sid = UUID.randomUUID().toString();
    String refresh = jwtTokenProvider.createRefreshToken(userPk, sid);

    UserRefreshToken row = new UserRefreshToken();
    row.setUserPk(userPk);
    row.setSessionId(sid);
    row.setTokenHash(TokenHash.sha256Base64(refresh));
    row.setIssuedAt(LocalDateTime.now());
    row.setExpiresAt(LocalDateTime.now().plusDays(14));
    row.setRevoked(false);
    row.setUserAgent(request.getHeader("User-Agent"));
    row.setIpAddress(request.getRemoteAddr());

    repo.save(row);
    return refresh;
  }

  @Override
  public UserRefreshToken findBySid(String sid) {
    return repo.findBySessionId(sid).orElse(null);
  }

  @Override
  public String rotate(Integer userPk, String sid) {
    UserRefreshToken row = repo.findBySessionId(sid).orElseThrow();
    String newRefresh = jwtTokenProvider.createRefreshToken(userPk, sid);

    row.setTokenHash(TokenHash.sha256Base64(newRefresh));
    row.setIssuedAt(LocalDateTime.now());
    row.setExpiresAt(LocalDateTime.now().plusDays(14));
    repo.save(row);

    return newRefresh;
  }

  @Override
  public void revoke(String sid) {
    repo.findBySessionId(sid).ifPresent(row -> {
      row.setRevoked(true);
      row.setRevokedAt(LocalDateTime.now());
      repo.save(row);
    });
  }
}
