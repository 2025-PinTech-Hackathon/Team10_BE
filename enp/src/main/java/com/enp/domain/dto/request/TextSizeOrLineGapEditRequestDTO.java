package com.enp.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextSizeOrLineGapEditRequestDTO {
    private Integer textSize;
    private Integer lineGap;
}
