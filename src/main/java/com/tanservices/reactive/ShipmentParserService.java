package com.tanservices.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
public class ShipmentParserService {

    protected Shipment getShipment(Map<String, String> row) {
        Shipment shipment = new Shipment();

        shipment.setTrackingCode(row.get("trackingNumber"));
        shipment.setCarrier(row.get("carrierName"));
        shipment.setCustomerFirstName(row.get("customerFirstName").isEmpty() ? "Random" : row.get("customerFirstName"));
        shipment.setCustomerLastName(row.get("customerLastName").isEmpty() ? "Random" : row.get("customerLastName"));
        shipment.setCustomerAddress(row.get("customerAddress").isEmpty() ? "Random" : row.get("customerAddress"));
        shipment.setCustomerEmail(row.get("customerEmail").isEmpty() ? "random@gmail.com" : row.get("customerEmail"));

        shipment.setCustomerPhone(row.get("customerPhone").isEmpty() ? "1234567890" : row.get("customerPhone"));
        shipment.setShopOrderId(row.get("order"));

        shipment.setWeight(Double.parseDouble(row.get("parcelWeight")));

        shipment.setWarehouse(row.get("warehouse"));
        shipment.setReferenceNumber(row.get("referenceNumber"));

        return shipment;
    }
}
