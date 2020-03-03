package com.unloadbrain.assignment.travix.integration.toughjet;

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
public class ToughJetFlightSearchService implements FlightSearchService {

    private final RestTemplate restTemplate;
    private final ToughJetIntegrationProperties properties;

    @Override
    public Flux<FlightSearchResponse> search(FlightSearchRequest flightSearchRequest) {

        ToughJetRequest apiRequest = new ToughJetRequest();
        apiRequest.setFrom(flightSearchRequest.getOrigin());
        apiRequest.setTo(flightSearchRequest.getDestination());
        apiRequest.setOutboundDate(flightSearchRequest.getDepartureDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        apiRequest.setInboundDate(flightSearchRequest.getReturnDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        apiRequest.setNumberOfAdults(flightSearchRequest.getNumberOfPassengers());

        ToughJetResponse[] apiResponse;
        try {
            apiResponse = restTemplate.postForObject(properties.getSearchUrl(), apiRequest, ToughJetResponse[].class);
        } catch (RestClientException ex) {
            log.debug("Could not fetch result from ToughJet API. Error: {}", ex);
            return Flux.just();
        }

        List<FlightSearchResponse> searchResultList = new ArrayList<>();
        for (ToughJetResponse toughJetResponse : apiResponse) {

            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("basePrice", BigDecimal.valueOf(toughJetResponse.getBasePrice()));
            additionalData.put("tax", BigDecimal.valueOf(toughJetResponse.getTax()));
            additionalData.put("discountPercentage", BigDecimal.valueOf(toughJetResponse.getDiscount()));

            FlightSearchResponse response = FlightSearchResponse.builder()
                    .airlineName(toughJetResponse.getCarrier())
                    .supplier("ToughJet")
                    .fare(calculateFare(toughJetResponse))
                    .departureAirportCode(toughJetResponse.getDepartureAirportName())
                    .destinationAirportCode(toughJetResponse.getArrivalAirportName())
                    .departureLocalDateTime(LocalDateTime.parse(toughJetResponse.getOutboundDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .arrivalLocalDateTime(LocalDateTime.parse(toughJetResponse.getInboundDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .additionalData(additionalData)
                    .build();
            searchResultList.add(response);
        }

        FlightSearchResponse[] searchResultArray = searchResultList.toArray(new FlightSearchResponse[searchResultList.size()]);
        return Flux.fromArray(searchResultArray);
    }

    private BigDecimal calculateFare(ToughJetResponse toughJetResponse) {
        BigDecimal discount = BigDecimal.valueOf(toughJetResponse.getBasePrice())
                .multiply(BigDecimal.valueOf(toughJetResponse.getDiscount())).divide(BigDecimal.valueOf(100));
        return BigDecimal.valueOf(toughJetResponse.getBasePrice())
                .add(BigDecimal.valueOf(toughJetResponse.getTax()))
                .subtract(discount);
    }
}
