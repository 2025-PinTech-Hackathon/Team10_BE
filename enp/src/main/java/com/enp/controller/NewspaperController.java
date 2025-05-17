package com.enp.controller;

import com.enp.domain.dto.response.NewsDetailDTO;
import com.enp.domain.dto.response.NewspaperResponseDTO;
import com.enp.service.NewspaperService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewspaperController {
    private final NewspaperService newspaperService;

    @GetMapping("")
    public ApiResponse<NewspaperResponseDTO> getNewsView(@AuthenticationPrincipal String loginId){
        return ApiResponse.onSuccess(newspaperService.getNewsView(loginId));
    }

    @GetMapping("/{newsId}")
    public ApiResponse<NewsDetailDTO> getNewsDetail(@AuthenticationPrincipal String loginId, @PathVariable Long newsId){
        return ApiResponse.onSuccess(newspaperService.getNewsDetail(loginId, newsId));
    }

}
