package com.household.backend.filter;

import com.household.backend.auth.AuthUser;
import com.household.backend.auth.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    String auth = request.getHeader("Authorization");

    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);

      try {
        if ("access".equals(jwtTokenProvider.getType(token))) {
          Integer userPk = jwtTokenProvider.getUserPk(token);

          List<SimpleGrantedAuthority> authorities =
              List.of(new SimpleGrantedAuthority("ROLE_USER"));

          Authentication authentication =
              new UsernamePasswordAuthenticationToken(userPk, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception ignored) {
        // 토큰 오류면 인증 안 올림
      }
    }

    filterChain.doFilter(request, response);
  }

}
