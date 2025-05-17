package com.enp.util;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String SECRET_KEY; // 실제 환경에서는 .env 등에서 관리

    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            // extractExpiration 내부에서 parseClaimsJws를 호출하므로,
            // 만료된 경우 여기서 ExpiredJwtException이 발생할 수 있습니다.
            // 이 경우, 명백히 만료된 것이므로 true를 반환합니다.
            return true;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        logger.debug("'{}' 사용자에 대한 토큰 유효성 검증 시도 중...", userDetails.getUsername());
        try {
            final String usernameFromToken = extractUsername(token); // 여기서 기본적인 파싱 및 서명 검증
            logger.debug("토큰에서 추출된 사용자 이름: {}", usernameFromToken);
            logger.debug("UserDetails의 사용자 이름: {}", userDetails.getUsername());

            boolean isUsernameMatch = usernameFromToken.equals(userDetails.getUsername());
            if (!isUsernameMatch) {
                logger.warn("JWT 사용자 이름 불일치. 토큰: [{}], UserDetails: [{}]", usernameFromToken, userDetails.getUsername());
            }

            // isTokenExpired는 내부적으로 extractExpiration을 호출하므로, 만료된 토큰이면 ExpiredJwtException을 잡거나 true를 반환
            boolean tokenIsNotExpired = !isTokenExpired(token);
            if (!tokenIsNotExpired) {
                logger.warn("JWT 토큰이 만료된 것으로 판단됨.");
            }

            boolean isValid = isUsernameMatch && tokenIsNotExpired;
            logger.info("'{}' 사용자에 대한 토큰 유효성 검증 결과: {}", userDetails.getUsername(), isValid);
            return isValid;

        } catch (MalformedJwtException e) {
            logger.error("잘못된 형식의 JWT 토큰: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // isTokenExpired에서 true를 반환하거나, extractUsername에서 직접 발생할 수 있음
            logger.warn("만료된 JWT 토큰 (validateToken에서 직접 감지): {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 이 예외는 보통 token 문자열이 null이거나 비어있을 때 Jwts.parserBuilder() 내부에서 발생
            logger.error("JWT 클레임 문자열이 비어있거나 부적절함: {}", e.getMessage());
        } catch (Exception e) { // 기타 예외 처리
            logger.error("토큰 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
        }
        return false; // 예외 발생 시 유효하지 않은 것으로 간주
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // 최신 API 사용
                .setSigningKey(SECRET_KEY) // Key 객체 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
