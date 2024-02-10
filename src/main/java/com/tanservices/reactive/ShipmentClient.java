package com.tanservices.reactive;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ShipmentClient {

    private final WebClient webClient;

    public ShipmentClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<String>> createShipment(Shipment shipment) {
        return webClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(shipment))
                .retrieve()
                .toEntity(String.class);
    }
}

