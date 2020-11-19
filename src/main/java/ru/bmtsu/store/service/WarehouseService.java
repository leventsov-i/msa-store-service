package ru.bmtsu.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.store.exception.NotFoundItemException;
import ru.bmtsu.store.exception.NotFoundOrderException;
import ru.bmtsu.store.exception.OrderServiceNotAvailableException;
import ru.bmtsu.store.exception.WarehouseServiceNotAvailableException;
import ru.bmtsu.store.service.dto.ItemInfoDTO;
import ru.bmtsu.store.service.dto.OrderResponseDTO;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseService {
    private final RestTemplate restTemplate;
    private final String host;
    private final String path;


    @Autowired
    public WarehouseService(RestTemplate restTemplate,
                            @Value("${service.warehouse.host}") String host,
                            @Value("${service.warehouse.path}") String path) {
        this.restTemplate = restTemplate;
        this.host = host;
        this.path = path;
    }

    public Optional<ItemInfoDTO> getItem(UUID itemUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid)
                .build()
                .encode()
                .toUri();

        try {
            ResponseEntity<ItemInfoDTO> order = restTemplate.getForEntity(uri, ItemInfoDTO.class);
            return Optional.ofNullable(order.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            } else {
                throw new WarehouseServiceNotAvailableException();
            }
        }
    }
}
