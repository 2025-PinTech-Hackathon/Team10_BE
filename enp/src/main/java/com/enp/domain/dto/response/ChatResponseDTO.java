package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
@Data
@Builder
public class ChatResponseDTO {
    private List<ChattingDTO> chattingDTOList;
    private Integer textSize;
    private Integer lineGap;

    @Builder
    public static class ChattingDTO{
        private Boolean isAI;
        private String content;
        private Timestamp date;
    }
}

