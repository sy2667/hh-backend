package com.household.backend.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    private String email;
    private String password;
    private String nickName;
}
