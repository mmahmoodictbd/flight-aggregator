package com.unloadbrain.assignment.travix.integration.spec.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class FlightSearchResponse {

    private String airlineName;

    private String supplier;

    private BigDecimal fare;

    private String departureAirportCode;

    private String destinationAirportCode;

    private LocalDateTime departureLocalDateTime;

    private LocalDateTime arrivalLocalDateTime;

    private Map<String, Object> additionalData;
}
