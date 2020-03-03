package com.unloadbrain.assignment.travix.busyflights.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BusyFlightsResponse {

    private String airlineName;

    private String supplier;

    private String fare;

    private String departureAirportCode;

    private String destinationAirportCode;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

}
