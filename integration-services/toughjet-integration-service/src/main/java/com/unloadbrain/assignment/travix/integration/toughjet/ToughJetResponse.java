package com.unloadbrain.assignment.travix.integration.toughjet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToughJetResponse {

    private String carrier;

    private String departureAirportName;

    private String arrivalAirportName;

    private String outboundDateTime;

    private String inboundDateTime;

    private double basePrice;

    private double tax;

    private double discount;

}
