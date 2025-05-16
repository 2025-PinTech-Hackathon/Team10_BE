package com.enp.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {
    private String token;
    private Long userId;
    private Boolean isLogin;
    private String nickname;
    private String loginId;
}
