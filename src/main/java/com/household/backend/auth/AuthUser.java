package com.household.backend.auth;

import lombok.Getter;

@Getter
public class AuthUser {
  private final Integer userPk;

  public AuthUser(Integer userPk) {
    this.userPk = userPk;
  }
}
