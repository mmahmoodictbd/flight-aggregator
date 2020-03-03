package com.unloadbrain.assignment.travix.busyflights.dto;

import com.unloadbrain.assignment.travix.busyflights.validator.IATACode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BusyFlightsRequest {

    @NotNull(message = "origin can't be null.")
    @Size(max = 3, min = 3, message = "origin length should be 3.")
    @IATACode(message = "origin should be a valid IATA code.")
    private String origin;


    @NotNull(message = "destination can't be null.")
    @Size(max = 3, min = 3, message = "destination length should be 3.")
    @IATACode(message = "destination should be a valid IATA code.")
    private String destination;

    @NotNull(message = "departureDate can't be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "departureDate should be present or future date.")
    private LocalDate departureDate;

    @NotNull(message = "returnDate can't be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "returnDate should be present or future date.")
    private LocalDate returnDate;

    @Positive(message = "numberOfPassengers should be positive.")
    private int numberOfPassengers;

}
