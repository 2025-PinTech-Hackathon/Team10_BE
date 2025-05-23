package com.enp.controller;

import com.enp.domain.dto.request.QuizSolveRequestDTO;
import com.enp.domain.dto.response.QuizResponseDTO;
import com.enp.domain.dto.response.QuizSolveResponseDTO;
import com.enp.service.QuizService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("")
    public ApiResponse<QuizResponseDTO> getQuizView(@AuthenticationPrincipal String loginId){
        return ApiResponse.onSuccess(quizService.getQuizView(loginId));
    }

    @PostMapping("/solve")
    public ApiResponse<QuizSolveResponseDTO> solveQuizView(@AuthenticationPrincipal String loginId, @RequestBody QuizSolveRequestDTO quizSolveRequestDTO){
        return ApiResponse.onSuccess(quizService.solveQuizView(loginId, quizSolveRequestDTO));
    }
}
