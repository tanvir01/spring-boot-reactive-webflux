package com.tanservices.reactive;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentResponse {
    private String message;
    private int totalRows = 0;
    private int successfulShipments = 0;
    private int failedShipments = 0;
    private List<String> failedTrackingNumbers;
}

