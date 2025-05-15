package com.enp.domain.dto.response;

import lombok.Builder;

@Builder
public class QuizResponseDTO {
    private Long quizId;
    private String content;
    private Integer todayQuizCount;
    private Integer textSize;
    private Integer lineGap;
}
