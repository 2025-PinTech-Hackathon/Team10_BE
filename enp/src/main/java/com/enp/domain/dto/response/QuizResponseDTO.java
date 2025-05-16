package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizResponseDTO {
    private Long quizId;
    private String content;
    private Integer todayQuizCount;
}
