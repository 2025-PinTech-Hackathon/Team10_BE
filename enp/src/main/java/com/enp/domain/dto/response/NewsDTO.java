package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewsDTO{
    private Long NewspaperId;
    private String title;
    private String summary;
}