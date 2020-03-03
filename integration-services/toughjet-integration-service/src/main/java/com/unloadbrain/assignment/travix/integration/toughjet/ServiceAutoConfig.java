package com.unloadbrain.assignment.travix.integration.toughjet;

import com.unloadbrain.assignment.travix.integration.spec.FlightSearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@AllArgsConstructor
public class ServiceAutoConfig {

    private final Environment environment;
    private final RestTemplateBuilder builder;

    @Bean
    public FlightSearchService toughJetFlightSearchService() {
        return new ToughJetFlightSearchService(restTemplate(), toughJetIntegrationProperties());
    }

    @Bean
    @Qualifier("toughJetRestTemplate")
    public RestTemplate restTemplate() {

        ToughJetIntegrationProperties properties = toughJetIntegrationProperties();

        return builder.setConnectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }

    @Bean
    public ToughJetIntegrationPropertiesReader toughJetIntegrationPropertiesReader() {
        return new ToughJetIntegrationPropertiesReader(environment);
    }

    @Bean
    public ToughJetIntegrationProperties toughJetIntegrationProperties() {
        return toughJetIntegrationPropertiesReader().read();
    }

}
