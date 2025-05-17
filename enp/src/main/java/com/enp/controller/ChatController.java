package com.enp.controller;

import com.enp.domain.dto.request.ChatSendRequestDTO;
import com.enp.domain.dto.response.ChatResponseDTO;
import com.enp.domain.dto.response.ChatSendResponseDTO;
import com.enp.service.ChatService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @GetMapping("")
    public ApiResponse<ChatResponseDTO> getChatView(@AuthenticationPrincipal String loginId){
        return ApiResponse.onSuccess(chatService.getChatView(loginId));
    }

    @PostMapping("/send")
    public ApiResponse<ChatSendResponseDTO> sendChat(@AuthenticationPrincipal String loginId, @RequestBody ChatSendRequestDTO chatSendRequestDTO) throws JSONException {
        return ApiResponse.onSuccess(chatService.sendChat(loginId, chatSendRequestDTO));
    }
}
