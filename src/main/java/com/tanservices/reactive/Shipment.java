package com.tanservices.reactive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Shipment {
    private String trackingCode;
    private String carrier;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhone;
    private String shopOrderId;
    private double weight;
    private String warehouse;
    private String referenceNumber;
}