package com.household.backend.controller;

import com.household.backend.auth.JwtTokenProvider;
import com.household.backend.common.CookieUtils;
import com.household.backend.common.TokenHash;
import com.household.backend.config.CookieProperties;
import com.household.backend.dto.req.NaverLoginReq;
import com.household.backend.dto.req.UserJoinCreate;
import com.household.backend.dto.req.UserLogin;
import com.household.backend.dto.req.UserUpdate;
import com.household.backend.dto.res.LoginRes;
import com.household.backend.dto.res.TokenRes;
import com.household.backend.dto.res.UserRes;
import com.household.backend.entity.User;
import com.household.backend.service.OauthService;
import com.household.backend.service.RefreshTokenService;
import com.household.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final OauthService oauthService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final CookieProperties cookieProperties;

  private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
      .httpOnly(true)
      .secure(cookieProperties.secure()) // TO-DO 로컬 http면 false, 운영 https면 true
      .sameSite("Lax")
      .path("/api/users")
      .maxAge(Duration.ofDays(14))
      .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  @PostMapping("/login/loginWithHome")
  public ResponseEntity<LoginRes> loginWithHome(@RequestBody UserLogin req, HttpServletResponse response, HttpServletRequest request) {
    User user = userService.loginWithHome(req);

    String access = jwtTokenProvider.createAccessToken(user.getUserPk());
    String refresh = refreshTokenService.issueAndStore(user.getUserPk(), request);

    setRefreshCookie(response, refresh);

    return ResponseEntity.ok(new LoginRes(UserRes.from(user), access));
  }

  @PostMapping("/login/naver")
  public ResponseEntity<LoginRes> naverLogin(@RequestBody NaverLoginReq req, HttpServletResponse response, HttpServletRequest request) {

    User user = oauthService.naverLogin(req.getCode(), req.getState());

    String access = jwtTokenProvider.createAccessToken(user.getUserPk());
    String refresh = refreshTokenService.issueAndStore(user.getUserPk(), request);

    setRefreshCookie(response, refresh);

    return ResponseEntity.ok(new LoginRes(UserRes.from(user), access));
  }

  @PostMapping("/login/joinWithHome")
  public ResponseEntity<LoginRes> joinWithHome(@RequestBody UserJoinCreate req, HttpServletResponse response, HttpServletRequest request) {
    User user = userService.joinWithHome(req);

    String access = jwtTokenProvider.createAccessToken(user.getUserPk());
    String refresh = refreshTokenService.issueAndStore(user.getUserPk(), request);

    setRefreshCookie(response, refresh);

    return ResponseEntity.ok(new LoginRes(UserRes.from(user), access));
  }


  @GetMapping("/{userPk}")
  public ResponseEntity<UserRes> getUser(@AuthenticationPrincipal Integer userPk) {
      return userService.findById(userPk)
              .map(user -> ResponseEntity.ok(UserRes.from(user)))
              .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{userPk}")
  public ResponseEntity<UserRes> updateUser(
      @AuthenticationPrincipal Integer userPk,
          @RequestBody UserUpdate req
  ) {
      User user = userService.updateUser(userPk, req);
      return ResponseEntity.ok(UserRes.from(user));
  }

  @DeleteMapping("/{userPk}")
  public ResponseEntity<Void> withdrawUser(@AuthenticationPrincipal Integer userPk) {
      userService.withdrawUser(userPk);
      return ResponseEntity.noContent().build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response, HttpServletRequest request) {
    String refreshToken = CookieUtils.get(request, "refresh_token");

    if (refreshToken != null) {
      try {
        if ("refresh".equals(jwtTokenProvider.getType(refreshToken))) {
          String sid = jwtTokenProvider.getSessionId(refreshToken);
          if (sid != null) refreshTokenService.revoke(sid);
        }
      } catch (Exception ignored) {}
    }

    ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .secure(cookieProperties.secure())
        .sameSite("Lax")
        .path("/api/users")
        .maxAge(0)
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/auth/me")
  public ResponseEntity<UserRes> me(@AuthenticationPrincipal Integer userPk) {
    User user = userService.findById(userPk)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

    return ResponseEntity.ok(UserRes.from(user));
  }

  @PostMapping("/auth/refresh")
  public ResponseEntity<TokenRes> refresh(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = CookieUtils.get(request, "refresh_token");
    if (refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.status(401).build();
    }

    try {
      if (!"refresh".equals(jwtTokenProvider.getType(refreshToken))) {
        return ResponseEntity.status(401).build();
      }

      Integer userPk = jwtTokenProvider.getUserPk(refreshToken);
      String sid = jwtTokenProvider.getSessionId(refreshToken);
      if (sid == null || sid.isBlank()) return ResponseEntity.status(401).build();

      var row = refreshTokenService.findBySid(sid);
      if (row == null || row.isRevoked()) return ResponseEntity.status(401).build();

      String incomingHash = TokenHash.sha256Base64(refreshToken);
      if (!incomingHash.equals(row.getTokenHash())) {
        refreshTokenService.revoke(sid);
        return ResponseEntity.status(401).build();
      }

      String newRefresh = refreshTokenService.rotate(userPk, sid);
      setRefreshCookie(response, newRefresh);

      String newAccess = jwtTokenProvider.createAccessToken(userPk);
      return ResponseEntity.ok(new TokenRes(newAccess));

    } catch (Exception e) {
      return ResponseEntity.status(401).build();
    }
  }


}
