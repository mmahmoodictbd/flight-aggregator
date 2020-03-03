package com.unloadbrain.assignment.travix.busyflights.api;

import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsRequest;
import com.unloadbrain.assignment.travix.busyflights.dto.BusyFlightsResponse;
import com.unloadbrain.assignment.travix.busyflights.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;


/**
 * Provides API endpoints for Search.
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/search")
public class SearchApi {

    private final SearchService searchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<BusyFlightsResponse> search(@RequestBody @Valid BusyFlightsRequest request) {
        return searchService.searchFlights(request);
    }

}
