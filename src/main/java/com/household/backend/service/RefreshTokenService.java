package com.household.backend.service;

import com.household.backend.entity.UserRefreshToken;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
  String issueAndStore(Integer userPk, HttpServletRequest request);
  UserRefreshToken findBySid(String sid);
  String rotate(Integer userPk, String sid);
  void revoke(String sid);
}
