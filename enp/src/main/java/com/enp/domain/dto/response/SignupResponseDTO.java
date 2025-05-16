package com.enp.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {
    private Long userId;
    private Boolean isDuplicated;
    private String nickname;
    private String loginId;
}
