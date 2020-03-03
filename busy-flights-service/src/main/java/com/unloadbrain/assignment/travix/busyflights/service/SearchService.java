package com.unloadbrain.assignment.travix.busyflights.service;

import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsRequest;
import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsResponse;
import com.unloadbrain.assignment.travix.integration.spec.FlightSearchService;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchRequest;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class SearchService {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private final List<FlightSearchService> flightSearchServices;

    public Flux<BusyFlightsResponse> searchFlights(BusyFlightsRequest busyFlightsRequest) {

        FlightSearchRequest flightSearchRequest = FlightSearchRequest.builder()
                .origin(busyFlightsRequest.getOrigin())
                .destination(busyFlightsRequest.getDestination())
                .departureDate(busyFlightsRequest.getDepartureDate())
                .returnDate(busyFlightsRequest.getReturnDate())
                .numberOfPassengers(busyFlightsRequest.getNumberOfPassengers())
                .build();

        List<Flux<FlightSearchResponse>> fightSearchResponseList = new ArrayList<>();
        flightSearchServices.parallelStream().forEach(flightSearchService ->
                fightSearchResponseList.add(flightSearchService.search(flightSearchRequest))
        );

        Flux<FlightSearchResponse> mergedResponses = Flux.concat(fightSearchResponseList)
                .sort(Comparator.comparing(FlightSearchResponse::getFare));

        return mergedResponses.map(this::buildBusyFlightsResponse);
    }

    private BusyFlightsResponse buildBusyFlightsResponse(FlightSearchResponse flightSearchResponse) {
        return BusyFlightsResponse.builder()
                .airlineName(flightSearchResponse.getAirlineName())
                .supplier(flightSearchResponse.getSupplier())
                .departureAirportCode(flightSearchResponse.getDepartureAirportCode())
                .destinationAirportCode(flightSearchResponse.getDestinationAirportCode())
                .departureDateTime(flightSearchResponse.getDepartureLocalDateTime())
                .arrivalDateTime(flightSearchResponse.getArrivalLocalDateTime())
                .fare(DECIMAL_FORMAT.format(flightSearchResponse.getFare()))
                .build();
    }
}
