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
    public ChatResponseDTO getChatView(Long userId) {
        List<ChatResponseDTO.ChattingDTO> chattingDTOList = new ArrayList<>();
        List<Chat> chatList = chatRepository.findAllByUserId(userId);

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

        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("채팅 화면 조회 "+userId + "인 사용자를 찾을 수 없습니다."));
        Integer textSize = user.getTextSize();
        Integer lineGap = user.getLineGap();

        return ChatResponseDTO.builder()
                .chattingDTOList(chattingDTOList)
                .textSize(textSize)
                .lineGap(lineGap)
                .build();
    }

    public ChatSendResponseDTO sendChat(Long userId, ChatSendRequestDTO chatSendRequestDTO) throws JSONException {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("채팅 메시지 전송 "+userId + "인 사용자를 찾을 수 없습니다."));
        Chat userChat = Chat.builder()
                .content(chatSendRequestDTO.getContent())
                .date(chatSendRequestDTO.getDate())
                .isAI(false)
                .user(user)
                .build();
        chatRepository.save(userChat);

        //String getResponseContent = gptService.askGpt(chatSendRequestDTO.getContent());
        String getResponseContent = "임시 대답";
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
