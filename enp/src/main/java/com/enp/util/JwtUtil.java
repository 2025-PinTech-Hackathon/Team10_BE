package com.enp.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtil {
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
        try {
            final String usernameFromToken = extractUsername(token); // 여기서 만료/서명 등 기본적인 검증이 일어남
            return (usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // 최신 API 사용
                .setSigningKey(SECRET_KEY) // Key 객체 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
