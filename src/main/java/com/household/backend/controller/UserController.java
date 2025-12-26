package com.household.backend.controller;

import com.household.backend.common.SessionUtils;
import com.household.backend.dto.req.NaverLoginReq;
import com.household.backend.dto.req.UserJoinCreate;
import com.household.backend.dto.req.UserLogin;
import com.household.backend.dto.req.UserUpdate;
import com.household.backend.dto.res.UserRes;
import com.household.backend.entity.User;
import com.household.backend.service.OauthService;
import com.household.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final OauthService oauthService;

  @PostMapping("/login/loginWithHome")
  public ResponseEntity<UserRes> loginWithHome(@RequestBody UserLogin req, HttpSession session) {
      User user = userService.loginWithHome(req);
    SessionUtils.setLoginUser(session, user.getUserPk());

      return ResponseEntity.ok(UserRes.from(user));
  }

  @PostMapping("/login/naver")
  public ResponseEntity<UserRes> naverLogin(@RequestBody NaverLoginReq req, HttpSession session) {

    User user = oauthService.naverLogin(req.getCode(), req.getState());

    SessionUtils.setLoginUser(session, user.getUserPk());

    return ResponseEntity.ok(UserRes.from(user));
  }

  @PostMapping("/login/joinWithHome")
  public ResponseEntity<UserRes> joinWithHome(@RequestBody UserJoinCreate req, HttpSession session) {
    User user = userService.joinWithHome(req);
    SessionUtils.setLoginUser(session, user.getUserPk());

    return ResponseEntity.ok(UserRes.from(user));
  }


  @GetMapping("/{userPk}")
  public ResponseEntity<UserRes> getUser(@PathVariable Integer userPk) {
      return userService.findById(userPk)
              .map(user -> ResponseEntity.ok(UserRes.from(user)))
              .orElse(ResponseEntity.notFound().build());
  }

  // ✅ PATCH로 변경
  @PatchMapping("/{userPk}")
  public ResponseEntity<UserRes> updateUser(
          @PathVariable Integer userPk,
          @RequestBody UserUpdate req
  ) {
      User user = userService.updateUser(userPk, req);
      return ResponseEntity.ok(UserRes.from(user));
  }

  @DeleteMapping("/{userPk}")
  public ResponseEntity<Void> withdrawUser(@PathVariable Integer userPk) {
      userService.withdrawUser(userPk);
      return ResponseEntity.noContent().build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/auth/me")
  public ResponseEntity<UserRes> me(HttpSession session) {
    Integer userPk = SessionUtils.getLoginUserPk(session); // 로그인 안 되어 있으면 예외 던지게

    User user = userService.findById(userPk)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

    return ResponseEntity.ok(UserRes.from(user));
  }


}
