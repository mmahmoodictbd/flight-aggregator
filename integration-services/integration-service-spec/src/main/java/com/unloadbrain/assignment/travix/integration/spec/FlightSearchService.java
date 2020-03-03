package com.unloadbrain.assignment.travix.integration.spec;

import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchRequest;
import com.unloadbrain.assignment.travix.integration.spec.dto.FlightSearchResponse;
import reactor.core.publisher.Flux;

public interface FlightSearchService {

    Flux<FlightSearchResponse> search(FlightSearchRequest flightSearchRequest);
}
