package com.unloadbrain.assignment.travix.busyflights.config;

import com.unloadbrain.assignment.travix.busyflights.Application;
import com.unloadbrain.assignment.travix.integration.crazyair.CrazyAirResponse;
import com.unloadbrain.assignment.travix.integration.toughjet.ToughJetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("it, stub")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class StubConfigIT {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void verifyIfCrazyAirStubWorks() {

        CrazyAirResponse[] response =
                restTemplate.postForObject("http://localhost:2345/search", new HashMap(), CrazyAirResponse[].class);

        assertEquals(2, response.length);
        assertEquals("KLM", response[0].getAirline());
        assertEquals("Turkish Airlines", response[1].getAirline());

    }

    @Test
    public void verifyIfToughJetStubWorks() {

        ToughJetResponse[] response =
                restTemplate.postForObject("http://localhost:2346/search", new HashMap(), ToughJetResponse[].class);

        assertEquals(2, response.length);
        assertEquals("KLM", response[0].getCarrier());
        assertEquals("Turkish Airlines", response[1].getCarrier());

    }
}