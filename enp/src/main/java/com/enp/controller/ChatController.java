package com.enp.controller;

import com.enp.domain.dto.response.ChatResponseDTO;
import com.enp.service.ChatService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/{userId}")
    public ApiResponse<ChatResponseDTO> getChatView(@PathVariable Long userId){
        return ApiResponse.onSuccess(chatService.getChatView(userId));
    }
}
