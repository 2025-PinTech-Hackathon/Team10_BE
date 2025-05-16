package com.enp.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
@Data
@Builder
public class ChatResponseDTO {
    private List<ChattingDTO> chatList;

    @Data
    @Builder
    public static class ChattingDTO{
        private Boolean isAI;
        private String content;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Timestamp date;
    }
}

