package com.tanservices.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ShipmentService {

    private final ShipmentClient shipmentClient;

    private final ShipmentParserService shipmentParserService;

    public ShipmentService(ShipmentClient shipmentClient, ShipmentParserService shipmentParserService) {
        this.shipmentClient = shipmentClient;
        this.shipmentParserService = shipmentParserService;
    }

    public Mono<ShipmentResponse> createShipmentsMono(MultipartFile file) {
        AtomicInteger successfulShipments = new AtomicInteger();
        AtomicInteger failedShipments = new AtomicInteger();
        List<String> failedTrackingNumbers = Collections.synchronizedList(new ArrayList<>());

        return Flux.using(() -> new BufferedReader(new InputStreamReader(file.getInputStream())),
                        reader -> Flux.fromStream(reader.lines()),
                        reader -> { try { reader.close(); } catch (IOException e) { throw new UncheckedIOException(e); } })
                .collectList()
                .flatMapMany(lines -> {
                    String firstLine = lines.get(0);
                    String delimiter = firstLine.contains(";") ? ";" : ",";
                    String[] headers = firstLine.split(delimiter);
                    return Flux.fromIterable(lines.subList(1, lines.size()))
                            .flatMap(line -> {
                                String[] values = line.split(",");
                                Map<String, String> row = new HashMap<>();

                                for (int i = 0; i < headers.length && i < values.length; i++) {
                                    row.put(headers[i], values[i].trim());
                                }

                                Mono<ResponseEntity<String>> shipmentMono = Mono.empty();

                                try {
                                    Shipment shipment = this.shipmentParserService.getShipment(row);
                                    log.info("Parsed shipment: {}", shipment);

                                    shipmentMono = createShipmentMono(shipment)
                                            .doOnSuccess(response -> {
                                                if (response != null && response.getStatusCode().is2xxSuccessful()) {
                                                    successfulShipments.incrementAndGet();
                                                } else {
                                                    failedTrackingNumbers.add(shipment.getTrackingCode());
                                                    failedShipments.incrementAndGet();
                                                }
                                            });
                                } catch (Exception e) {
                                    log.error("Failed to parse row: {}", row, e);
                                    failedShipments.incrementAndGet();
                                    failedTrackingNumbers.add(row.get("trackingNumber"));
                                }

                                return shipmentMono;
                            });
                })
                .then(Mono.defer(() -> {  // This block will be executed after all items have been emitted
                    ShipmentResponse shipmentResponse = new ShipmentResponse();
                    shipmentResponse.setSuccessfulShipments(successfulShipments.get());
                    shipmentResponse.setFailedShipments(failedShipments.get());
                    shipmentResponse.setTotalRows(successfulShipments.get() + failedShipments.get());
                    shipmentResponse.setFailedTrackingNumbers(failedTrackingNumbers);
                    shipmentResponse.setMessage("Shipment creation completed");
                    return Mono.just(shipmentResponse);
                }));

    }


    protected Mono<ResponseEntity<String>> createShipmentMono(Shipment shipment) {
        log.info("Creating shipment: {}", shipment);
        return shipmentClient.createShipment(shipment)
                .doOnNext(response -> log.info("Response from createShipment: {}", response))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("Failed to create shipment", e);
                    String responseBody = e.getResponseBodyAsString();
                    log.error("Response body: {}", responseBody);
                    return Mono.empty();
                });
    }

}

