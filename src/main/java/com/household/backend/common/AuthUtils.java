package com.household.backend.common;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {

  public static Integer getLoginUserPk(HttpServletRequest request) {
    Integer userPk = (Integer) request.getAttribute("userPk");

    if (userPk == null) {
      throw new RuntimeException("로그인이 필요합니다.");
    }

    return userPk;
  }
}
