package com.enp.controller;

import com.enp.domain.dto.response.QuizResponseDTO;
import com.enp.service.QuizService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;
    @GetMapping("/{userId}")
    public ApiResponse<QuizResponseDTO> getQuizView(@PathVariable long userId){
        return ApiResponse.onSuccess(quizService.getQuizView(userId));
    }
}
