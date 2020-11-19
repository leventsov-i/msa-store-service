package ru.bmtsu.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmtsu.store.controller.dto.OrderInfoDTO;
import ru.bmtsu.store.controller.dto.PurchaseRequestDTO;
import ru.bmtsu.store.controller.dto.WarrantyRequestDTO;
import ru.bmtsu.store.controller.dto.WarrantyResponseDTO;
import ru.bmtsu.store.entity.User;
import ru.bmtsu.store.repository.UserRepository;
import ru.bmtsu.store.service.dto.ItemInfoDTO;
import ru.bmtsu.store.service.dto.OrderResponseDTO;
import ru.bmtsu.store.service.dto.WarrantyInfoDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final WarrantyService warrantyService;

    @Autowired
    public StoreService(UserRepository userRepository, OrderService orderService,
                        WarehouseService warehouseService, WarrantyService warrantyService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.warehouseService = warehouseService;
        this.warrantyService = warrantyService;

        //todo delete after demo
        userRepository.save(new User(null, "Alex", UUID.fromString("6d2cb5a0-943c-4b96-9aa6-89eac7bdfd2b")));
    }


    public OrderInfoDTO getOrder(UUID userUid, UUID orderUid) {
        OrderResponseDTO order = orderService.getOrder(userUid, orderUid);
        Optional<ItemInfoDTO> item = warehouseService.getItem(order.getItemUid());
        Optional<WarrantyInfoDTO> warrantyInfo = warrantyService.getItemWarrantyInfo(order.getItemUid());

        return OrderInfoDTO.from(order, item, warrantyInfo);
    }

    public List<OrderInfoDTO> getOrders(UUID userUid) {
        return Arrays.stream(orderService.getOrders(userUid))
                .map(order -> {
                    Optional<ItemInfoDTO> item = warehouseService.getItem(order.getItemUid());
                    Optional<WarrantyInfoDTO> warrantyInfo = warrantyService.getItemWarrantyInfo(order.getItemUid());
                    return OrderInfoDTO.from(order, item, warrantyInfo);
                })
                .collect(Collectors.toList());
    }

    public void refund(UUID userUid, UUID orderUid) {
        orderService.refundOrder(orderUid);
    }

    public WarrantyResponseDTO warranty(UUID orderUid, WarrantyRequestDTO warranty) {
        WarrantyResponseDTO responseDTO = orderService.warranty(orderUid, warranty);
        responseDTO.setOrderUid(orderUid);
        return responseDTO;
    }

    public UUID purchase(UUID userUid, PurchaseRequestDTO purchase) {
        return orderService.purchase(purchase, userUid).getOrderUid();
    }
}
