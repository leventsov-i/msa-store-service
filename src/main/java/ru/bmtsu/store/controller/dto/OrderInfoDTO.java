package ru.bmtsu.store.controller.dto;

import lombok.*;
import ru.bmtsu.store.service.dto.ItemInfoDTO;
import ru.bmtsu.store.service.dto.OrderResponseDTO;
import ru.bmtsu.store.service.dto.WarrantyInfoDTO;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderInfoDTO {
    private UUID orderUid;
    private String date;
    private String model;
    private String size;
    private String warrantyDate;
    private String warrantyStatus;

    public static OrderInfoDTO from(OrderResponseDTO order, Optional<ItemInfoDTO> item, Optional<WarrantyInfoDTO> warrantyInfo) {
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        orderInfoDTO.setOrderUid(order.getOrderUid());
        orderInfoDTO.setDate(order.getOrderDate());

        item.ifPresent(itemInfo -> {
            orderInfoDTO.setModel(itemInfo.getModel());
            orderInfoDTO.setSize(itemInfo.getSize());
        });

        warrantyInfo.ifPresent(warranty -> {
            orderInfoDTO.setWarrantyDate(warranty.getWarrantyDate());
            orderInfoDTO.setWarrantyStatus(warranty.getStatus());
        });

        return orderInfoDTO;
    }
}
