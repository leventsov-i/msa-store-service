package ru.bmtsu.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.store.controller.dto.PurchaseRequestDTO;
import ru.bmtsu.store.controller.dto.WarrantyRequestDTO;
import ru.bmtsu.store.controller.dto.WarrantyResponseDTO;
import ru.bmtsu.store.exception.NotFoundOrderException;
import ru.bmtsu.store.exception.OrderServiceNotAvailableException;
import ru.bmtsu.store.service.dto.CreateOrderResponseDTO;
import ru.bmtsu.store.service.dto.OrderResponseDTO;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final RestTemplate restTemplate;
    private final String host;
    private final String path;

    @Autowired
    public OrderService(RestTemplate restTemplate,
                        @Value("${service.order.host}") String host,
                        @Value("${service.order.path}") String path) {
        this.restTemplate = restTemplate;
        this.host = host;
        this.path = path;
    }

    public OrderResponseDTO getOrder(UUID userUid, UUID orderUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + userUid + "/" + orderUid)
                .build()
                .encode()
                .toUri();

        try {
            ResponseEntity<OrderResponseDTO> order = restTemplate.getForEntity(uri, OrderResponseDTO.class);
            return order.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundOrderException();
            } else {
                throw new OrderServiceNotAvailableException();
            }
        }
    }

    public OrderResponseDTO[] getOrders(UUID userUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + userUid)
                .build()
                .encode()
                .toUri();

        try {
            ResponseEntity<OrderResponseDTO[]> order = restTemplate.getForEntity(uri, OrderResponseDTO[].class);
            return order.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundOrderException();
            } else {
                throw new OrderServiceNotAvailableException();
            }
        }
    }

    public void refundOrder(UUID orderUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + orderUid)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(uri);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundOrderException();
            } else {
                throw new OrderServiceNotAvailableException();
            }
        }
    }

    public WarrantyResponseDTO warranty(UUID orderUid, WarrantyRequestDTO warrantyRequestDTO) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + orderUid + "/warranty")
                .build()
                .encode()
                .toUri();
        try {
            ResponseEntity<WarrantyResponseDTO> warranty = restTemplate.postForEntity(uri, warrantyRequestDTO, WarrantyResponseDTO.class);
            return warranty.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundOrderException();
            } else {
                throw new OrderServiceNotAvailableException();
            }
        }
    }

    public CreateOrderResponseDTO purchase(PurchaseRequestDTO purchase, UUID userUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + userUid)
                .build()
                .encode()
                .toUri();
        try {
            ResponseEntity<CreateOrderResponseDTO> warranty = restTemplate.postForEntity(uri, purchase, CreateOrderResponseDTO.class);
            return warranty.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundOrderException();
            } else {
                throw new OrderServiceNotAvailableException();
            }
        }
    }
}
