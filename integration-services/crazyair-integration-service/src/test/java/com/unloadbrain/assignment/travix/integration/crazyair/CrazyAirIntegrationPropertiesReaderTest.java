package com.unloadbrain.assignment.travix.integration.crazyair;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrazyAirIntegrationPropertiesReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldBuildCrazyAirIntegrationPropertiesFromEnvironmentProperties() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.crazyair.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.crazyair.auth.apiKey", "apiKey1")
                .withProperty("integration.crazyair.auth.apiSecret", "apiSecret1")
                .withProperty("integration.crazyair.resttemplate.connectionTimeoutMs", "1000")
                .withProperty("integration.crazyair.resttemplate.readTimeoutMs", "2000");

        // When
        CrazyAirIntegrationProperties properties = new CrazyAirIntegrationPropertiesReader(environment).read();

        // Then
        assertEquals("https://somedomain.com/search", properties.getSearchUrl());
        assertEquals("apiKey1", properties.getAuthApiKey());
        assertEquals("apiSecret1", properties.getAuthApiSecret());
        assertEquals(1000, properties.getConnectionTimeout());
        assertEquals(2000, properties.getReadTimeout());
    }

    @Test
    public void shouldBuildCrazyAirIntegrationPropertiesWithDefaultConnectionTimeoutMsAndReadTimeoutMs() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.crazyair.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.crazyair.auth.apiKey", "apiKey1")
                .withProperty("integration.crazyair.auth.apiSecret", "apiSecret1");

        // When
        CrazyAirIntegrationProperties properties = new CrazyAirIntegrationPropertiesReader(environment).read();

        // Then
        assertEquals(3000, properties.getConnectionTimeout());
        assertEquals(3000, properties.getReadTimeout());
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfSearchUrlNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.crazyair.searchUrl is not defined.");

        // When
        new CrazyAirIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfApiKeyNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.crazyair.searchUrl", "https://somedomain.com/search");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.crazyair.auth.apiKey is not defined.");

        // When
        new CrazyAirIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfApiSecretNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.crazyair.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.crazyair.auth.apiKey", "apiKey1");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.crazyair.auth.apiSecret is not defined.");

        // When
        new CrazyAirIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }
}