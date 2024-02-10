package com.tanservices.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ShipmentServiceTest {

    @Mock
    private ShipmentClient shipmentClient;

    @Mock
    private ShipmentParserService shipmentParserService;

    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateShipmentsMono() throws Exception {
        // Arrange
        // Create a sample CSV file with all columns
        String csvData = "trackingNumber,carrierName,customerFirstName,customerLastName,customerAddress,customerEmail,customerPhone,order,parcelWeight,warehouse,referenceNumber\n" +
                "12345,FedEx,John,Doe,123 Main St,johndoe@example.com,1234567890,Order123,2.5,WarehouseA,Ref123\n";

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvData.getBytes());
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Shipment created", HttpStatus.OK);

        when(shipmentParserService.getShipment(any())).thenCallRealMethod();
        when(shipmentClient.createShipment(any())).thenReturn(Mono.just(responseEntity));

        // Act
        Mono<ShipmentResponse> result = shipmentService.createShipmentsMono(file);

        // Assert
        StepVerifier.create(result)
                .assertNext(shipmentResponse -> {
                    assertEquals(1, shipmentResponse.getSuccessfulShipments());
                    assertEquals(0, shipmentResponse.getFailedShipments());
                    assertEquals(1, shipmentResponse.getTotalRows());
                    assertTrue(shipmentResponse.getFailedTrackingNumbers().isEmpty());
                    assertEquals("Shipment creation completed", shipmentResponse.getMessage());
                })
                .verifyComplete();
    }

    @Test
    public void testCreateShipmentsMonoFailure() throws Exception {
        // Arrange
        // Create a sample CSV file with all columns
        String csvData = "trackingNumber,carrierName,customerFirstName,customerLastName,customerAddress,customerEmail,customerPhone,order,parcelWeight,warehouse,referenceNumber\n" +
                "12345,FedEx,John,Doe,123 Main St,johndoe@example.com,1234567890,Order123,2.5,WarehouseA,Ref123\n";

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvData.getBytes());
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Shipment creation failed", HttpStatus.INTERNAL_SERVER_ERROR);

        when(shipmentParserService.getShipment(any())).thenCallRealMethod();
        when(shipmentClient.createShipment(any())).thenReturn(Mono.just(responseEntity));

        // Act
        Mono<ShipmentResponse> result = shipmentService.createShipmentsMono(file);

        // Assert
        StepVerifier.create(result)
                .assertNext(shipmentResponse -> {
                    assertEquals(0, shipmentResponse.getSuccessfulShipments());
                    assertEquals(1, shipmentResponse.getFailedShipments());
                    assertEquals(1, shipmentResponse.getTotalRows());
                    assertFalse(shipmentResponse.getFailedTrackingNumbers().isEmpty());
                    assertEquals("Shipment creation completed", shipmentResponse.getMessage());
                })
                .verifyComplete();
    }
}
