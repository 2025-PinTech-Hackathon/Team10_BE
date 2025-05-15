package com.enp.service;

import com.enp.domain.dto.response.ChatResponseDTO;
import com.enp.domain.entity.Chat;
import com.enp.domain.entity.User;
import com.enp.repository.ChatRepository;
import com.enp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Data
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    public ChatResponseDTO getChatView(Long userId) {
        List<ChatResponseDTO.ChattingDTO> chattingDTOList = new ArrayList<>();
        List<Chat> chatList = chatRepository.findAll();

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

        User user = userRepository.findById(userId).get();
        Integer textSize = user.getTextSize();
        Integer lineGap = user.getLineGap();

        return ChatResponseDTO.builder()
                .chattingDTOList(chattingDTOList)
                .textSize(textSize)
                .lineGap(lineGap)
                .build();
    }
}
