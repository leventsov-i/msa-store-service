package ru.bmtsu.store.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WarrantyInfoDTO {
    private String itemUid;
    private String warrantyDate;
    private String status;
}
