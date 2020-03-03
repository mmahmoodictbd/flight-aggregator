package com.unloadbrain.assignment.travix.busyflights.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Provides stub related beans.
 */
@Slf4j
@Profile("stub")
@Configuration
public class StubConfig {

    @Bean
    public WireMockServer crazyAirStubServer() {

        log.info("Enabling CrazyAir request response stubbing.");

        WireMockServer wireMockServer = new WireMockServer(options().port(2345));
        wireMockServer.stubFor(post("/search").willReturn(okJson(readContentAsString("stubs/crazyair.response.json"))));
        wireMockServer.start();
        return wireMockServer;
    }

    @Bean
    public WireMockServer toughJetStubServer() {

        log.info("Enabling ToughJet request response stubbing.");

        WireMockServer wireMockServer = new WireMockServer(options().port(2346));
        wireMockServer.stubFor(post("/search").willReturn(okJson(readContentAsString("stubs/toughjet.response.json"))));
        wireMockServer.start();
        return wireMockServer;
    }

    private String readContentAsString(String path) {
        InputStream is = getResourceFileAsInputStream(path);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("Could not read the file.");
        }
    }

    private InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = StubConfig.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
