package ru.bmtsu.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.store.exception.NotFoundOrderException;
import ru.bmtsu.store.exception.OrderServiceNotAvailableException;
import ru.bmtsu.store.exception.WarrantlyServiceNotAvailableException;
import ru.bmtsu.store.service.dto.OrderResponseDTO;
import ru.bmtsu.store.service.dto.WarrantyInfoDTO;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarrantyService {
    private final RestTemplate restTemplate;
    private final String host;
    private final String path;

    @Autowired
    public WarrantyService(RestTemplate restTemplate,
                            @Value("${service.warranty.host}") String host,
                            @Value("${service.warranty.path}") String path) {
        this.restTemplate = restTemplate;
        this.host = host;
        this.path = path;
    }

    public Optional<WarrantyInfoDTO> getItemWarrantyInfo(UUID itemUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid)
                .build()
                .encode()
                .toUri();

        try {
            ResponseEntity<WarrantyInfoDTO> order = restTemplate.getForEntity(uri, WarrantyInfoDTO.class);
            return Optional.ofNullable(order.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            } else {
                throw new WarrantlyServiceNotAvailableException();
            }
        }
    }
}
