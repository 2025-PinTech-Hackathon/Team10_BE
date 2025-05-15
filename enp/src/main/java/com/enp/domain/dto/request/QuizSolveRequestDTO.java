package com.enp.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizSolveRequestDTO {
    private Long quizId;
    private String answer;
}
