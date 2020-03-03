package com.unloadbrain.assignment.travix.integration.toughjet;

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
public class ToughJetFlightSearchServiceTest {

    private final RestTemplate restTemplateMock;
    private final ToughJetFlightSearchService toughJetFlightSearchService;
    private final ToughJetIntegrationProperties toughJetIntegrationPropertiesMock;

    public ToughJetFlightSearchServiceTest() {
        this.restTemplateMock = mock(RestTemplate.class);
        this.toughJetIntegrationPropertiesMock = mock(ToughJetIntegrationProperties.class);
        this.toughJetFlightSearchService = new ToughJetFlightSearchService(restTemplateMock, toughJetIntegrationPropertiesMock);
    }

    @Test
    public void shouldReturnFlightSearchResponse() {

        // Given

        ToughJetResponse toughJetResponse = new ToughJetResponse();
        toughJetResponse.setCarrier("Turkish Airlines");
        toughJetResponse.setDepartureAirportName("DAC");
        toughJetResponse.setArrivalAirportName("AMS");
        toughJetResponse.setOutboundDateTime("2021-01-01T10:15:30");
        toughJetResponse.setInboundDateTime("2021-01-01T12:45:00");
        toughJetResponse.setBasePrice(100);
        toughJetResponse.setTax(20);
        toughJetResponse.setDiscount(5);

        when(toughJetIntegrationPropertiesMock.getSearchUrl()).thenReturn("mockedSearchUrl");
        when(restTemplateMock.postForObject(anyString(), any(ToughJetRequest.class), any(Class.class)))
                .thenReturn(new ToughJetResponse[]{toughJetResponse});

        // When

        FlightSearchRequest flightSearchRequest = FlightSearchRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();
        Flux<FlightSearchResponse> flightSearchResponse = toughJetFlightSearchService.search(flightSearchRequest);

        // Then
        StepVerifier.create(flightSearchResponse).expectNextMatches(response ->
                "Turkish Airlines".equals(response.getAirlineName())
                        && "ToughJet".equals(response.getSupplier())
                        && new BigDecimal("115.00").equals(response.getFare())
                        && "DAC".equals(response.getDepartureAirportCode())
                        && "AMS".equals(response.getDestinationAirportCode())
                        && LocalDateTime.parse("2021-01-01T10:15:30").isEqual(response.getDepartureLocalDateTime())
                        && LocalDateTime.parse("2021-01-01T12:45:00").isEqual(response.getArrivalLocalDateTime())
                        && BigDecimal.valueOf(100.0).equals(response.getAdditionalData().get("basePrice"))
                        && BigDecimal.valueOf(20.0).equals(response.getAdditionalData().get("tax"))
                        && BigDecimal.valueOf(5.0).equals(response.getAdditionalData().get("discountPercentage")))
                .expectComplete().verify();

    }

    @Test
    public void shouldReturnEmptyArrayAsFlightSearchResponseWhenRestClientThrowException() {

        // Given

        when(toughJetIntegrationPropertiesMock.getSearchUrl()).thenReturn("mockedSearchUrl");
        doThrow(new RestClientException("Something bad"))
                .when(restTemplateMock).postForObject(anyString(), any(ToughJetRequest.class), any(Class.class));

        // When

        FlightSearchRequest flightSearchRequest = FlightSearchRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();
        Flux<FlightSearchResponse> flightSearchResponse = toughJetFlightSearchService.search(flightSearchRequest);

        // Then
        StepVerifier.create(flightSearchResponse).expectNextCount(0).expectComplete().verify();
    }
}