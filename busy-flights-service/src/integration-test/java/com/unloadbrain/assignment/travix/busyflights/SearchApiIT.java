package com.unloadbrain.assignment.travix.busyflights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unloadbrain.assignment.travix.busyflights.api.SearchApi;
import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsRequest;
import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsResponse;
import com.unloadbrain.assignment.travix.busyflights.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(SearchApi.class)
@ActiveProfiles("it")
public class SearchApiIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SearchService searchService;

    @Test
    public void shouldReturnSearchResult() throws Exception {

        BusyFlightsResponse response = BusyFlightsResponse.builder()
                .airlineName("Emirates")
                .supplier("CrazyAir")
                .departureAirportCode("DAC")
                .destinationAirportCode("AMS")
                .departureDateTime(LocalDateTime.of(2021, 01, 01, 10, 0, 0))
                .arrivalDateTime(LocalDateTime.of(2021, 01, 01, 11, 0, 0))
                .fare("820.50")
                .build();

        when(searchService.searchFlights(any())).thenReturn(Flux.just(response));

        BusyFlightsRequest request = BusyFlightsRequest.builder()
                .origin("DAC")
                .destination("AMS")
                .departureDate(LocalDate.of(2021, 01, 01))
                .returnDate(LocalDate.of(2021, 01, 20))
                .numberOfPassengers(1)
                .build();


        webTestClient.post().uri("/api/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString((request)))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].airlineName").isEqualTo("Emirates")
                .jsonPath("$[0].supplier").isEqualTo("CrazyAir")
                .jsonPath("$[0].departureAirportCode").isEqualTo("DAC")
                .jsonPath("$[0].destinationAirportCode").isEqualTo("AMS")
                .jsonPath("$[0].departureDateTime").isEqualTo("2021-01-01T10:00:00")
                .jsonPath("$[0].arrivalDateTime").isEqualTo("2021-01-01T11:00:00")
                .jsonPath("$[0].fare").isEqualTo("820.50");


    }

}
