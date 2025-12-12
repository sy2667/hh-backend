package com.household.backend.oauth;

import com.household.backend.dto.res.NaverProfileRes;
import com.household.backend.dto.res.NaverTokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverOauthClient {

  private final RestTemplate restTemplate;

  @Value("${naver.client-id}")
  private String clientId;

  @Value("${naver.client-secret}")
  private String clientSecret;

  @Value("${naver.redirect-uri}")
  private String redirectUri;

  private static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
  private static final String PROFILE_URL = "https://openapi.naver.com/v1/nid/me";

  public NaverTokenRes getAccessToken(String code, String state) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);
    params.add("code", code);
    params.add("state", state);

    String url = TOKEN_URL + buildQueryString(params);

    return restTemplate.getForObject(url, NaverTokenRes.class);
  }

  public NaverProfileRes getUserProfile(String accessToken) {
    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);

    org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);

    org.springframework.http.ResponseEntity<NaverProfileRes> response =
        restTemplate.exchange(
            PROFILE_URL,
            org.springframework.http.HttpMethod.GET,
            entity,
            NaverProfileRes.class
        );

    return response.getBody();
  }

  private String buildQueryString(MultiValueMap<String, String> params) {
    StringBuilder sb = new StringBuilder("?");
    params.forEach((key, values) -> {
      for (String value : values) {
        if (sb.length() > 1) sb.append("&");
        sb.append(key).append("=").append(value);
      }
    });
    return sb.toString();
  }

}
