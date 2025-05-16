package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class NewsDetailDTO {
    private String title;
    private String content;
    private String reporter;
    private Timestamp date;
}
