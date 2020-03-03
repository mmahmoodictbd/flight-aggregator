package com.unloadbrain.assignment.travix.integration.crazyair;

import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchRequest;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrazyAirFlightSearchServiceTest {

    private final RestTemplate restTemplateMock;
    private final CrazyAirFlightSearchService crazyAirFlightSearchService;
    private final CrazyAirIntegrationProperties crazyAirIntegrationPropertiesMock;

    public CrazyAirFlightSearchServiceTest() {
        this.restTemplateMock = mock(RestTemplate.class);
        this.crazyAirIntegrationPropertiesMock = mock(CrazyAirIntegrationProperties.class);
        this.crazyAirFlightSearchService = new CrazyAirFlightSearchService(restTemplateMock, crazyAirIntegrationPropertiesMock);
    }

    @Test
    public void shouldReturnFlightSearchResponse() {

        // Given

        CrazyAirResponse crazyAirResponse = new CrazyAirResponse();
        crazyAirResponse.setAirline("Turkish Airlines");
        crazyAirResponse.setDepartureAirportCode("DAC");
        crazyAirResponse.setDestinationAirportCode("AMS");
        crazyAirResponse.setDepartureDate("2021-01-01T10:15:30");
        crazyAirResponse.setArrivalDate("2021-01-01T12:45:00");
        crazyAirResponse.setCabinClass("E");
        crazyAirResponse.setPrice(850.01);

        when(crazyAirIntegrationPropertiesMock.getSearchUrl()).thenReturn("mockedSearchUrl");
        when(restTemplateMock.postForObject(anyString(), any(CrazyAirRequest.class), any(Class.class)))
                .thenReturn(new CrazyAirResponse[]{crazyAirResponse});

        // When

        FlightSearchRequest flightSearchRequest = FlightSearchRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();
        Flux<FlightSearchResponse> flightSearchResponse = crazyAirFlightSearchService.search(flightSearchRequest);

        // Then
        StepVerifier.create(flightSearchResponse).expectNextMatches(response ->
                "Turkish Airlines".equals(response.getAirlineName())
                        && "CrazyAir".equals(response.getSupplier())
                        && BigDecimal.valueOf(850.01).equals(response.getFare())
                        && "DAC".equals(response.getDepartureAirportCode())
                        && "AMS".equals(response.getDestinationAirportCode())
                        && LocalDateTime.parse("2021-01-01T10:15:30").isEqual(response.getDepartureLocalDateTime())
                        && LocalDateTime.parse("2021-01-01T12:45:00").isEqual(response.getArrivalLocalDateTime())
                        && "E".equals(response.getAdditionalData().get("cabinClass")))
                .expectComplete().verify();

    }

    @Test
    public void shouldReturnEmptyArrayAsFlightSearchResponseWhenRestClientThrowException() {

        // Given

        when(crazyAirIntegrationPropertiesMock.getSearchUrl()).thenReturn("mockedSearchUrl");
        doThrow(new RestClientException("Something bad"))
                .when(restTemplateMock).postForObject(anyString(), any(CrazyAirRequest.class), any(Class.class));

        // When

        FlightSearchRequest flightSearchRequest = FlightSearchRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();
        Flux<FlightSearchResponse> flightSearchResponse = crazyAirFlightSearchService.search(flightSearchRequest);

        // Then
        StepVerifier.create(flightSearchResponse).expectNextCount(0).expectComplete().verify();
    }
}