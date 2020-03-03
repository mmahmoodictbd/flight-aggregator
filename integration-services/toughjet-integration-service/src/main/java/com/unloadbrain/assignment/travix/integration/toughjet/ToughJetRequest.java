package com.unloadbrain.assignment.travix.integration.toughjet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToughJetRequest {

    private String from;

    private String to;

    private String outboundDate;

    private String inboundDate;

    private int numberOfAdults;
}
