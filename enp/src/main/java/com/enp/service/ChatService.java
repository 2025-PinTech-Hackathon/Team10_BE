package com.enp.service;

import com.enp.domain.dto.request.ChatSendRequestDTO;
import com.enp.domain.dto.response.ChatResponseDTO;
import com.enp.domain.dto.response.ChatSendResponseDTO;
import com.enp.domain.entity.Chat;
import com.enp.domain.entity.User;
import com.enp.repository.ChatRepository;
import com.enp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final GptService gptService;
    public ChatResponseDTO getChatView(String loginId) {
        List<ChatResponseDTO.ChattingDTO> chattingDTOList = new ArrayList<>();
        List<Chat> chatList = chatRepository.findAllByUser(userRepository.findByLoginId(loginId).get());

        for(Chat chat : chatList){
            Boolean isAI = chat.getIsAI();
            String content = chat.getContent();
            Timestamp date = chat.getDate();
            ChatResponseDTO.ChattingDTO chattingDTO = ChatResponseDTO.ChattingDTO.builder()
                    .isAI(isAI)
                    .content(content)
                    .date(date)
                    .build();

            chattingDTOList.add(chattingDTO);
        }

        User user = userRepository.findByLoginId(loginId).orElseThrow(()->new RuntimeException("채팅 화면 조회 "+loginId + "인 사용자를 찾을 수 없습니다."));

        return ChatResponseDTO.builder()
                .chatList(chattingDTOList)
                .build();
    }

    public ChatSendResponseDTO sendChat(String loginId, ChatSendRequestDTO chatSendRequestDTO) throws JSONException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(()->new RuntimeException("채팅 메시지 전송 " + loginId + "인 사용자를 찾을 수 없습니다."));
        Chat userChat = Chat.builder()
                .content(chatSendRequestDTO.getContent())
                .date(chatSendRequestDTO.getDate())
                .isAI(false)
                .user(user)
                .build();
        chatRepository.save(userChat);

        String getResponseContent = gptService.askGpt(chatSendRequestDTO.getContent());
        Timestamp gptResponseDate = new Timestamp(System.currentTimeMillis());

        Chat gptChat = Chat.builder()
                .content(getResponseContent)
                .date(gptResponseDate)
                .isAI(true)
                .user(user)
                .build();
        chatRepository.save(gptChat);

        return ChatSendResponseDTO.builder()
                .content(gptChat.getContent())
                .date(gptChat.getDate())
                .build();
    }
}
