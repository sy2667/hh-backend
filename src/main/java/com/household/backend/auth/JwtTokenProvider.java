package com.household.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final long accessExpMs;
  private final long refreshExpMs;

  public JwtTokenProvider(JwtProperties props) {
    this.key = Keys.hmacShaKeyFor(
        props.secret().getBytes(StandardCharsets.UTF_8)
    );

    this.accessExpMs =
        Duration.ofMinutes(props.accessExpMin()).toMillis();

    this.refreshExpMs =
        Duration.ofDays(props.refreshExpDays()).toMillis();
  }

  public String createRefreshToken(Integer userPk, String sessionId) {
    return createToken(userPk, "refresh", refreshExpMs, sessionId);
  }

  public String createAccessToken(Integer userPk) {
    return createToken(userPk, "access", accessExpMs, null);
  }

  private String createToken(Integer userPk, String type, long expMs, String sessionId) {
    long now = System.currentTimeMillis();

    var builder = Jwts.builder()
        .subject(String.valueOf(userPk))
        .claims(Map.of("type", type))
        .issuedAt(new Date(now))
        .expiration(new Date(now + expMs));

    if (sessionId != null) {
      builder.claim("sid", sessionId);
    }

    return builder
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  public String getSessionId(String token) {
    return parse(token).getPayload().get("sid", String.class);
  }

  public Integer getUserPk(String token) {
    Claims claims = parse(token).getPayload();
    return Integer.valueOf(claims.getSubject());
  }

  public String getType(String token) {
    return parse(token).getPayload().get("type", String.class);
  }

  private Jws<Claims> parse(String token) {
    return Jwts.parser()
        .verifyWith((javax.crypto.SecretKey) key)
        .build()
        .parseSignedClaims(token);
  }
}
