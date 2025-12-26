package com.household.backend.service.impl;

import com.household.backend.dto.req.UserJoinCreate;
import com.household.backend.dto.req.UserLogin;
import com.household.backend.dto.req.UserUpdate;
import com.household.backend.entity.User;
import com.household.backend.repository.UserRepository;
import com.household.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loginWithHome(UserLogin req) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(req.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;

    }

    @Override
    public Optional<User> findById(Integer userPk) {
        return userRepository.findById(userPk);
    }

    @Override
    @Transactional
    public User updateUser(Integer userPk, UserUpdate req) {
        User user = userRepository.findById(userPk).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if(req.getNickName() != null) {
            user.setNickName(req.getNickName());
        }
        if(req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void withdrawUser(Integer userPk) {
        User user = userRepository.findById(userPk).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.withdraw();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User joinWithHome(UserJoinCreate req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        try {
            User user = new User();
            user.setEmail(req.getEmail());
            user.setNickName(req.getNickname());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setProvider("HOME");

          return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

    }

}
