package ru.bmtsu.store.controller.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WarrantyResponseDTO {
    @Setter
    private UUID orderUid;
    private String warrantyDate;
    private String decision;
}
