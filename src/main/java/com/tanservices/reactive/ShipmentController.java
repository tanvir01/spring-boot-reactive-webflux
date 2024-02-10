package com.tanservices.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/shipments")
@Slf4j
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }


    @PostMapping("/csv-create")
    public Mono<ResponseEntity<ShipmentResponse>> createShipments(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ShipmentResponse shipmentResponse = new ShipmentResponse();
            shipmentResponse.setMessage("File is empty");
            return Mono.just(ResponseEntity.badRequest().body(shipmentResponse));
        }

        return shipmentService.createShipmentsMono(file)
                .doOnNext(response -> log.info("Response: {}", response))
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(e -> {
                    log.error("Error: {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }

}
