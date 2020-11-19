package ru.bmtsu.store.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateOrderResponseDTO {
    private UUID orderUid;
}
