package com.unloadbrain.assignment.travix.busyflights.service;

import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsRequest;
import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsResponse;
import com.unloadbrain.assignment.travix.integration.spec.FlightSearchService;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchResponse;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

public class SearchServiceTest {

    @Test
    public void shouldAggregateIntegrationServiceResponsesAndProduceSortedResponse() {

        // Given

        FlightSearchService mockIntegrationService1 = buildMockIntegrationService1();
        FlightSearchService mockIntegrationService2 = buildMockIntegrationService2();
        SearchService searchService = new SearchService(Arrays.asList(mockIntegrationService1, mockIntegrationService2));

        // When

        BusyFlightsRequest request = BusyFlightsRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();

        Flux<BusyFlightsResponse> busyFlightsResponseFlux = searchService.searchFlights(request);

        // Then

        StepVerifier.create(busyFlightsResponseFlux)
                .expectNextMatches(
                        response -> "ABCAir".equals(response.getAirlineName())
                                && "supplier2".equals(response.getSupplier())
                                && "1.99".equals(response.getFare())
                                && "ValidIATACode1".equals(response.getDepartureAirportCode())
                                && "ValidIATACode2".equals(response.getDestinationAirportCode())
                                && LocalDateTime.parse("2021-01-01T01:00:00").isEqual(response.getDepartureDateTime())
                                && LocalDateTime.parse("2021-01-01T02:00:00").isEqual(response.getArrivalDateTime()))
                .expectNextMatches(
                        response -> "CoolAir".equals(response.getAirlineName())
                                && "supplier1".equals(response.getSupplier())
                                && "2.00".equals(response.getFare())
                                && "ValidIATACode1".equals(response.getDepartureAirportCode())
                                && "ValidIATACode2".equals(response.getDestinationAirportCode())
                                && LocalDateTime.parse("2021-01-01T01:00:00").isEqual(response.getDepartureDateTime())
                                && LocalDateTime.parse("2021-01-01T02:00:00").isEqual(response.getArrivalDateTime()))
                .expectNextMatches(
                        response -> "XYZAir".equals(response.getAirlineName())
                                && "supplier2".equals(response.getSupplier())
                                && "2.01".equals(response.getFare())
                                && "ValidIATACode1".equals(response.getDepartureAirportCode())
                                && "ValidIATACode2".equals(response.getDestinationAirportCode())
                                && LocalDateTime.parse("2021-01-01T03:00:00").isEqual(response.getDepartureDateTime())
                                && LocalDateTime.parse("2021-01-01T04:00:00").isEqual(response.getArrivalDateTime()))
                .expectNextMatches(
                        response -> "HotAir".equals(response.getAirlineName())
                                && "supplier1".equals(response.getSupplier())
                                && "3.25".equals(response.getFare())
                                && "ValidIATACode1".equals(response.getDepartureAirportCode())
                                && "ValidIATACode2".equals(response.getDestinationAirportCode())
                                && LocalDateTime.parse("2021-01-01T03:00:00").isEqual(response.getDepartureDateTime())
                                && LocalDateTime.parse("2021-01-01T04:00:00").isEqual(response.getArrivalDateTime()))
                .expectComplete()
                .log().verify();
    }

    private FlightSearchService buildMockIntegrationService1() {

        return flightSearchRequest -> {

            FlightSearchResponse response1 = FlightSearchResponse.builder()
                    .airlineName("CoolAir")
                    .supplier("supplier1")
                    .fare(BigDecimal.valueOf(2.00))
                    .departureAirportCode("ValidIATACode1")
                    .destinationAirportCode("ValidIATACode2")
                    .departureLocalDateTime(LocalDateTime.parse("2021-01-01T01:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse("2021-01-01T02:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(Collections.emptyMap())
                    .build();

            FlightSearchResponse response2 = FlightSearchResponse.builder()
                    .airlineName("HotAir")
                    .supplier("supplier1")
                    .fare(BigDecimal.valueOf(3.25))
                    .departureAirportCode("ValidIATACode1")
                    .destinationAirportCode("ValidIATACode2")
                    .departureLocalDateTime(LocalDateTime.parse("2021-01-01T03:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse("2021-01-01T04:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(Collections.emptyMap())
                    .build();

            return Flux.just(response1, response2);
        };
    }

    private FlightSearchService buildMockIntegrationService2() {

        return flightSearchRequest -> {

            FlightSearchResponse response1 = FlightSearchResponse.builder()
                    .airlineName("ABCAir")
                    .supplier("supplier2")
                    .fare(BigDecimal.valueOf(1.99))
                    .departureAirportCode("ValidIATACode1")
                    .destinationAirportCode("ValidIATACode2")
                    .departureLocalDateTime(LocalDateTime.parse("2021-01-01T01:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse("2021-01-01T02:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(Collections.emptyMap())
                    .build();

            FlightSearchResponse response2 = FlightSearchResponse.builder()
                    .airlineName("XYZAir")
                    .supplier("supplier2")
                    .fare(BigDecimal.valueOf(2.01))
                    .departureAirportCode("ValidIATACode1")
                    .destinationAirportCode("ValidIATACode2")
                    .departureLocalDateTime(LocalDateTime.parse("2021-01-01T03:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse("2021-01-01T04:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(Collections.emptyMap())
                    .build();

            return Flux.just(response1, response2);
        };
    }
}