package com.example.backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${JWT.SECRET}")
    private String secretKey;

    private byte[] keyBytes;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3; // 3일

    // SECRET_KEY를 Base64로 디코딩하여 바이트 배열로 변환
    @PostConstruct
    public void init() {
        this.keyBytes = Base64.getDecoder().decode(secretKey);
    }

    // (공통) 토큰 생성 로직
    private String createToken(String loginId, Long memberId, long expireTime) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(loginId)
                .claim("memberId", memberId)  // memberId를 Claims에 추가
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS512)
                .compact();
    }

    // 액세스 토큰 생성 로직
    public String createAccessToken(String loginId, Long memberId) {
        log.info("Creating Access Token for loginId: {}", loginId);
        return createToken(loginId, memberId, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // 리프레시 토큰 생성 로직
    public String createRefreshToken(String loginId, Long memberId) {
        log.info("Creating Refresh Token for loginId: {}", loginId);
        return createToken(loginId, memberId, REFRESH_TOKEN_EXPIRE_TIME);
    }

    // 토큰에서 account 추출
    public String getLoginIdFromToken(String token) {
        log.info("Extracting loginId from token");
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰에서 memberId 추출
    public Long getMemberIdFromToken(String token) {
        log.info("Extracting memberId from token");
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("memberId", Long.class);
    }

    // 토큰 유효성 검증 로직
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyBytes).build().parseClaimsJws(token);
            log.info("토큰이 유효합니다.");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다."); // 만료된 토큰인 경우
            throw e;
        } catch (JwtException e) {
            log.warn("유효하지 않은 JWT 토큰입니다.");
        }
        return false; // 유효하지 않은 토큰인 경우 -> refreshToken
    }

    // 쿠키에서 액세스 토큰 추출
    public String resolveAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Redis에서 리프레시 토큰을 조회
    public String getRefreshTokenFromRedis(String loginId) {
        return (String) redisTemplate.opsForValue().get(loginId);
    }

    // 새로운 액세스 토큰을 쿠키에 설정
    public void setAccessTokenCookie(String accessToken, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(900); // 15분 만료
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
    }

    // 액세스 토큰 쿠키 만료 처리
    public void expireAccessTokenCookie(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null); // 쿠키 값을 null로 설정
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
    }

    // 토큰 만료 시간 가져오기
    public Date getExpirationDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}