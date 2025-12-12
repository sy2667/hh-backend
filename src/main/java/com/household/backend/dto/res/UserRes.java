package com.household.backend.dto.res;

import com.household.backend.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserRes {
    private Integer userPk;
    private String nickName;
    private String email;
    private String provider;
    private LocalDateTime createAt;

    public static UserRes from(User user) {
        return UserRes.builder()
                .userPk(user.getUserPk())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .provider(user.getProvider())
                .createAt(user.getCreatedAt())
                .build();
    }
}
