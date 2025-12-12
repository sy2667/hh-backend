package com.household.backend.service;

import com.household.backend.dto.res.NaverProfileRes;
import com.household.backend.dto.res.NaverTokenRes;
import com.household.backend.entity.User;
import com.household.backend.oauth.NaverOauthClient;
import com.household.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

  private final UserRepository userRepository;
  private final NaverOauthClient naverOauthClient;

  public User naverLogin(String code, String state) {
    NaverTokenRes token = naverOauthClient.getAccessToken(code, state);
    NaverProfileRes profile = naverOauthClient.getUserProfile(token.getAccessToken());

    String provider = "NAVER";
    String providerId = profile.getResponse().getId();

    return userRepository.findByProvider(provider, providerId)
      .orElseGet(() -> {
        User user = new User();
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setNickName(profile.getResponse().getNickname());
        user.setEmail(profile.getResponse().getEmail());
        return userRepository.save(user);
      });
  }

}
