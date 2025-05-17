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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService; // UserDetailsServiceImpl

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Authorization Header: {}", authHeader);

        String username = null; // 여기서 username은 loginId를 의미
        String jwt = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            logger.debug("Extracted JWT: {}", jwt);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.debug("Username extracted from JWT: {}", username);
            } catch (Exception e) {
                logger.warn("Could not extract username from JWT. Token: [{}], Error: {}", jwt, e.getMessage());
            }
        } else {
            logger.debug("Authorization header does not exist or does not start with Bearer String.");
        }

        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("SecurityContext is null for user '{}', attempting to authenticate.", username);
            UserDetails userDetails = null; // UserDetails는 여전히 필요 (예: 비밀번호, 활성화 상태 등 검증)
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    logger.debug("UserDetails loaded for username: '{}'. Authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());
                } else {
                    logger.warn("UserDetailsService returned null for username: '{}'", username);
                }
            } catch (Exception e) {
                logger.error("Error loading user details for username '{}': {}", username, e.getMessage());
            }

            if (userDetails != null) {
                boolean isValidToken = false;
                try {
                    isValidToken = jwtUtil.validateToken(jwt, userDetails);
                    logger.debug("JWT validation result for user '{}': {}", username, isValidToken);
                } catch (Exception e) {
                    logger.error("Error validating token for user '{}': {}", username, e.getMessage());
                }

                if (isValidToken) {
                    // --- 중요: Principal을 loginId 문자열로 설정 ---
                    // UserDetails에서 권한 정보를 가져옵니다.
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    if (authorities == null || authorities.isEmpty()) {
                        // UserDetailsServiceImpl에서 권한을 설정하지 않았다면 기본 권한 부여
                        logger.warn("No authorities found for user '{}', granting default ROLE_USER.", username);
                        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    // UsernamePasswordAuthenticationToken의 첫 번째 인자(principal)로 username(loginId 문자열)을 직접 전달합니다.
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("User '{}' authenticated successfully. Principal is String. SecurityContext updated.", username);
                } else {
                    logger.warn("JWT token validation failed for user: '{}'. Token: [{}]", username, jwt);
                }
            }
        } else {
            if (!StringUtils.hasText(username)) {
                logger.debug("Username is null or empty, skipping authentication for this request.");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                logger.debug("SecurityContext already contains an authentication for: '{}'", SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }
        chain.doFilter(request, response);
    }
}
