package com.household.backend.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserJoinCreate {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;
  private String nickname;
}
