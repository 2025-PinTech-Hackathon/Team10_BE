package com.enp.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextSizeOrLineGapCheckResponseDTO {
    private Long userId;
    private Integer textSize;
    private Integer lineGap;
}
