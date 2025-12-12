package com.household.backend.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverProfileRes {

  private String resultcode;
  private String message;
  private Response response;

  @Getter
  @Setter
  public static class Response {
    private String id;
    private String email;
    private String name;
    private String nickname;
  }

}
