package com.enp.config;

import com.enp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class); // 로거 선언

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService; // UserDetailsServiceImpl이 주입됨

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        // 요청 URI와 Authorization 헤더를 로그로 남겨 어떤 요청에서 토큰이 어떻게 들어오는지 확인합니다.
        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Authorization Header: {}", authHeader);

        String username = null;
        String jwt = null;

        // 1. Authorization 헤더에서 "Bearer " 접두사를 확인하고 JWT를 추출합니다.
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            logger.debug("Extracted JWT: {}", jwt);
            try {
                // 2. JWT에서 사용자 이름(loginId)을 추출합니다.
                username = jwtUtil.extractUsername(jwt); // JwtUtil의 extractUsername 호출
                logger.debug("Username extracted from JWT: {}", username);
            } catch (Exception e) {
                // 토큰 추출 중 예외 발생 시 (예: 만료, 서명 오류 등) 로그를 남기고 username은 null로 유지됩니다.
                logger.warn("Could not extract username from JWT. Token: [{}], Error: {}", jwt, e.getMessage());
            }
        } else {
            logger.debug("Authorization header does not exist or does not start with Bearer String.");
        }

        // 3. 사용자 이름이 성공적으로 추출되었고, 현재 SecurityContext에 인증 정보가 없는 경우에만 인증을 시도합니다.
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("SecurityContext is null for user '{}', attempting to authenticate.", username);
            UserDetails userDetails = null;
            try {
                // 4. UserDetailsService를 통해 데이터베이스에서 사용자 정보를 조회합니다.
                userDetails = userDetailsService.loadUserByUsername(username); // UserDetailsServiceImpl의 loadUserByUsername 호출
                if (userDetails != null) {
                    logger.debug("UserDetails loaded for username: '{}'. Authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());
                } else {
                    // 정상적인 경우 UserDetailsService는 사용자를 찾지 못하면 UsernameNotFoundException을 던집니다.
                    // null을 반환하는 경우는 드물지만, 방어적으로 로그를 남깁니다.
                    logger.warn("UserDetailsService returned null for username: '{}'", username);
                }
            } catch (Exception e) {
                logger.error("Error loading user details for username '{}': {}", username, e.getMessage());
            }

            // 5. UserDetails가 성공적으로 로드되었는지 확인합니다.
            if (userDetails != null) {
                boolean isValidToken = false;
                try {
                    // 6. JWT의 유효성을 검증합니다 (사용자 이름 일치, 만료 여부 등).
                    isValidToken = jwtUtil.validateToken(jwt, userDetails); // JwtUtil의 validateToken 호출
                    logger.debug("JWT validation result for user '{}': {}", username, isValidToken);
                } catch (Exception e) {
                    // validateToken 내부에서도 예외를 로깅하지만, 필터 레벨에서도 로깅할 수 있습니다.
                    logger.error("Error validating token for user '{}': {}", username, e.getMessage());
                }

                // 7. 토큰이 유효하면 Authentication 객체를 생성하여 SecurityContext에 설정합니다.
                if (isValidToken) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // (선택 사항) 요청 세부 정보를 Authentication 객체에 추가합니다.
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("User '{}' authenticated successfully. SecurityContext updated.", username);
                } else {
                    logger.warn("JWT token validation failed for user: '{}'. Token: [{}]", username, jwt);
                }
            }
        } else {
            // 사용자 이름이 없거나 이미 SecurityContext에 인증 정보가 있는 경우 로깅
            if (!StringUtils.hasText(username)) {
                logger.debug("Username is null or empty, skipping authentication for this request.");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                logger.debug("SecurityContext already contains an authentication for: '{}'", SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }

        // 다음 필터로 요청을 전달합니다.
        chain.doFilter(request, response);
    }
}
