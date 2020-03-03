package com.unloadbrain.assignment.travix.integration.crazyair;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrazyAirRequest {

    private String origin;

    private String destination;

    private String departureDate;

    private String returnDate;

    private int passengerCount;
}
