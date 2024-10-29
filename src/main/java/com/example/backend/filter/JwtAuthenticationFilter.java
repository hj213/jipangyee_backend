// JwtAuthenticationFilter.java
package com.example.backend.filter;

import com.example.backend.util.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        // 인증이 필요 없는 경로 예외 처리
        if (requestURI.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증 로직
        String token = resolveToken(request);

        if (token != null) {
            log.info("JWT Token found in request: {}", token);
            if (tokenProvider.validateToken(token)) {
                String account = tokenProvider.getAccountFromToken(token);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(account, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("JWT Token is valid. Authentication set for account: {}", account);
            } else {
                log.warn("Invalid JWT Token: {}", token);
            }
        } else {
            log.info("No JWT Token found in request");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
