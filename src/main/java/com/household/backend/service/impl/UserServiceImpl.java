package com.household.backend.service.impl;

import com.household.backend.dto.req.UserLogin;
import com.household.backend.dto.req.UserUpdate;
import com.household.backend.entity.User;
import com.household.backend.repository.UserRepository;
import com.household.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User loginOrRegister(UserLogin req) {
        Optional<User> exUser = userRepository.findByProvider(
                req.getProvider(),
                req.getProviderId()
        );

        if(exUser.isPresent()) {
            return exUser.get();
        } else {
            User newUser = new User();
            newUser.setProvider(req.getProvider());
            newUser.setProviderId(req.getProviderId());
            newUser.setEmail(req.getEmail());
            newUser.setNickName(req.getNickName());

            return userRepository.save(newUser);
        }
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

}
