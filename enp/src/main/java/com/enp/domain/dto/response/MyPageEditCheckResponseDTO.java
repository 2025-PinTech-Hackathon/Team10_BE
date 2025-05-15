package com.enp.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageEditCheckResponseDTO {
    private String nickname;
    private String loginId;
    private Integer textSize;
}
