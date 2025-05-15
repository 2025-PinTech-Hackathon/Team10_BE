package com.enp.service;

import com.enp.domain.dto.response.QuizResponseDTO;
import com.enp.domain.entity.Quiz;
import com.enp.domain.entity.User;
import com.enp.repository.QuizRepository;
import com.enp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Data
@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    public QuizResponseDTO getQuizView(long userId) {
        Quiz quiz = quizRepository.findRandomQuiz().orElseThrow();
        Long quizId = quiz.getId();
        String content = quiz.getContent();
        User user = userRepository.findById(userId).get();
        Integer todayQuizCount = user.getTodayQuizCount();
        Integer textSize = user.getTextSize();
        Integer lineGap = user.getLineGap();

        return QuizResponseDTO.builder()
                .quizId(quizId)
                .content(content)
                .todayQuizCount(todayQuizCount)
                .textSize(textSize)
                .lineGap(lineGap)
                .build();
    }
}
