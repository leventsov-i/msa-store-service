package ru.bmtsu.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bmtsu.store.controller.dto.*;
import ru.bmtsu.store.exception.*;
import ru.bmtsu.store.service.StoreService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{userUid}/{orderUid}")
    public ResponseEntity<OrderInfoDTO> getOrder(@PathVariable UUID userUid, @PathVariable UUID orderUid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getOrder(userUid, orderUid));
    }

    @GetMapping("/{userUid}/orders")
    public ResponseEntity<List<OrderInfoDTO>> getOrders(@PathVariable UUID userUid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getOrders(userUid));
    }

    @DeleteMapping("/{userUid}/{orderUid}/refund")
    public ResponseEntity refund(@PathVariable UUID userUid, @PathVariable UUID orderUid) {
        storeService.refund(userUid, orderUid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/{userUid}/{orderUid}/warranty")
    public ResponseEntity<WarrantyResponseDTO> warranty(@PathVariable UUID userUid, @PathVariable UUID orderUid, @RequestBody WarrantyRequestDTO warranty) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.warranty(orderUid, warranty));
    }

    @PostMapping("/{userUid}/purchase")
    public ResponseEntity purchase(@PathVariable UUID userUid, @RequestBody PurchaseRequestDTO purchase) {
        UUID orderUid = storeService.purchase(userUid, purchase);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderUid}")
                .buildAndExpand(orderUid)
                .toUri();

        return ResponseEntity
                .created(uri)
                .build();
    }

    @ExceptionHandler({NotFoundItemException.class, NotFoundOrderException.class})
    public ResponseEntity<ErrorDTO> handlerNotFoundException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    public ResponseEntity<ErrorDTO> handlerItemNotAvailableException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler({OrderServiceNotAvailableException.class, WarehouseServiceNotAvailableException.class, WarrantlyServiceNotAvailableException.class})
    public ResponseEntity<ErrorDTO> handlerServiceNotAvailableException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorDTO(e.getMessage()));
    }

}
