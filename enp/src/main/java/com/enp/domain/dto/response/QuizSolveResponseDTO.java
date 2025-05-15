package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizSolveResponseDTO {
    private Boolean isCorrect;
    private Long quizId;
    private String content;
    private Integer textSize;
    private Integer lineGap;
}
