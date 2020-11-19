package ru.bmtsu.store.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderResponseDTO {
    private UUID orderUid;
    private String orderDate;
    private UUID itemUid;
    private String status;
}
