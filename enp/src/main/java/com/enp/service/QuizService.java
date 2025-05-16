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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    public QuizResponseDTO getQuizView(Long userId) {
        Quiz quiz = quizRepository.findRandomQuiz().orElseThrow(()->new RuntimeException("퀴즈 화면: 랜덤 퀴즈를 찾을 수 없습니다. "));
        Long quizId = quiz.getId();
        String content = quiz.getContent();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("퀴즈 화면 "+userId + "인 사용자를 찾을 수 없습니다."));
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
        Quiz quiz = quizRepository.findById(quizSolveRequestDTO.getQuizId()).orElseThrow(()->new RuntimeException("퀴즈 풀이: 일치하는 퀴즈 아이디를 찾을 수 없습니다. "));
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("퀴즈 풀이 "+userId + "인 사용자를 찾을 수 없습니다."));

        Boolean isCorrect = quiz.getAnswer().equals(quizSolveRequestDTO.getAnswer());

        if(user.getTodayQuizCount()>0){
            if(isCorrect){
                user.setTodayQuizCount(user.getTodayQuizCount()-1);
                user.setPoint(user.getPoint()+5);
            }
            else{
                user.setPoint(user.getPoint()-3);
            }
        }else{
            user.setTodayQuizCount(0);
        }

        userRepository.save(user);
        Quiz randomQuiz = quizRepository.findRandomQuiz().get();

        Long quizId = randomQuiz.getId();
        String content = randomQuiz.getContent();
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
