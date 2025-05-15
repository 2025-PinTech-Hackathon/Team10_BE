package com.enp.service;

import com.enp.domain.dto.request.QuizSolveRequestDTO;
import com.enp.domain.dto.response.QuizResponseDTO;
import com.enp.domain.dto.response.QuizSolveResponseDTO;
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

    public QuizSolveResponseDTO solveQuizView(Long userId, QuizSolveRequestDTO quizSolveRequestDTO) {
        Quiz quiz = quizRepository.findById(quizSolveRequestDTO.getQuizId()).get();

        Boolean isCorrect = quiz.getAnswer().equals(quizSolveRequestDTO.getAnswer());
        Long quizId = quiz.getId();
        String content = quiz.getContent();

        User user = userRepository.findById(userId).get();
        Integer textSize = user.getTextSize();
        Integer lineGap = user.getLineGap();
        return QuizSolveResponseDTO.builder()
                .isCorrect(isCorrect)
                .quizId(quizId)
                .content(content)
                .textSize(textSize)
                .lineGap(lineGap)
                .build();
    }
}
