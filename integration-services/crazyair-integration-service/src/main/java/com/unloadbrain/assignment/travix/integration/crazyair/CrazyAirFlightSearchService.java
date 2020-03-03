package com.unloadbrain.assignment.travix.integration.crazyair;

import com.unloadbrain.assignment.travix.integration.spec.FlightSearchService;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchRequest;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class CrazyAirFlightSearchService implements FlightSearchService {

    private final RestTemplate restTemplate;
    private final CrazyAirIntegrationProperties properties;

    @Override
    public Flux<FlightSearchResponse> search(FlightSearchRequest flightSearchRequest) {

        CrazyAirRequest apiRequest = new CrazyAirRequest();
        apiRequest.setOrigin(flightSearchRequest.getOrigin());
        apiRequest.setDestination(flightSearchRequest.getDestination());
        apiRequest.setDepartureDate(flightSearchRequest.getDepartureDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        apiRequest.setReturnDate(flightSearchRequest.getReturnDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        apiRequest.setPassengerCount(flightSearchRequest.getNumberOfPassengers());

        CrazyAirResponse[] apiResponse;
        try {
            apiResponse = restTemplate.postForObject(properties.getSearchUrl(), apiRequest, CrazyAirResponse[].class);
        } catch (RestClientException ex) {
            log.debug("Could not fetch result from CrazyAir API. Error: {}", ex);
            return Flux.just();
        }

        List<FlightSearchResponse> searchResultList = new ArrayList<>();
        for (CrazyAirResponse crazyAirResponse : apiResponse) {

            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("cabinClass", crazyAirResponse.getCabinClass());

            FlightSearchResponse response = FlightSearchResponse.builder()
                    .airlineName(crazyAirResponse.getAirline())
                    .supplier("CrazyAir")
                    .fare(BigDecimal.valueOf(crazyAirResponse.getPrice()))
                    .departureAirportCode(crazyAirResponse.getDepartureAirportCode())
                    .destinationAirportCode(crazyAirResponse.getDestinationAirportCode())
                    .departureLocalDateTime(LocalDateTime.parse(crazyAirResponse.getDepartureDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse(crazyAirResponse.getArrivalDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(additionalData)
                    .build();
            searchResultList.add(response);
        }

        FlightSearchResponse[] searchResultArray = searchResultList.toArray(new FlightSearchResponse[searchResultList.size()]);
        return Flux.fromArray(searchResultArray);
    }
}
