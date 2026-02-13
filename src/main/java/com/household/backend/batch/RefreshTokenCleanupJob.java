package com.household.backend.batch;

import com.household.backend.repository.UserRefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {

  private final UserRefreshTokenRepository userRefreshTokenRepository;

  @Scheduled(cron = "0 0 3 ? * SUN", zone = "Asia/Seoul")
  @Transactional
  public void cleanup() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(30);

    int deleted = userRefreshTokenRepository
        .deleteExpiredAndOldRevoked(threshold);

    System.out.println("🧹 RefreshToken 정리 완료, 삭제된 개수: " + deleted);
  }

}
