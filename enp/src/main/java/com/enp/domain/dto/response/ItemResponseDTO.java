package com.enp.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {
    Boolean isPurchase;
    String itemName;
    Long reservedPoint;
}
