package com.enp.controller;

import com.enp.domain.dto.Response.NewspaperResponseDTO;
import com.enp.service.NewspaperService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewspaperController {
    private final NewspaperService newspaperService;

    @GetMapping("/{userId}")
    public ApiResponse<NewspaperResponseDTO> getNewsView(@PathVariable Long userId){
        return ApiResponse.onSuccess(newspaperService.getNewsView(userId));
    }

}
