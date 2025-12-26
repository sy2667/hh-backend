package com.household.backend.service;

import com.household.backend.dto.req.UserJoinCreate;
import com.household.backend.dto.req.UserLogin;
import com.household.backend.dto.req.UserUpdate;
import com.household.backend.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {

    // 소셜 로그인 - 사용자 조회 또는 생성
    User loginWithHome(UserLogin req);

    // 사용자 조회 (ID로)
    Optional<User> findById(Integer userPk);

    // 사용자 정보 수정
    User updateUser(Integer userPk, UserUpdate req);

    // 회원 탈퇴
    void withdrawUser(Integer userPk);

    public User joinWithHome(UserJoinCreate req);

}
