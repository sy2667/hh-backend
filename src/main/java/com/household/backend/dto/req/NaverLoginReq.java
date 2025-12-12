package com.household.backend.dto.req;

import lombok.Getter;

@Getter
public class NaverLoginReq {
  private String code;
  private String state;
}
