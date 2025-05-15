package com.enp.domain.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class NewspaperResponseDTO {
    private List<NewsDTO> newsList;
    private Integer textsize;
    private Integer linegap;

}

