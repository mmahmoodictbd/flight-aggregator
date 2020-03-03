package com.unloadbrain.assignment.travix.integration.toughjet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToughJetIntegrationPropertiesReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldBuildToughJetIntegrationPropertiesFromEnvironmentProperties() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.toughjet.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.toughjet.auth.username", "username1")
                .withProperty("integration.toughjet.auth.password", "password1")
                .withProperty("integration.toughjet.resttemplate.connectionTimeoutMs", "1000")
                .withProperty("integration.toughjet.resttemplate.readTimeoutMs", "2000");

        // When
        ToughJetIntegrationProperties properties = new ToughJetIntegrationPropertiesReader(environment).read();

        // Then
        assertEquals("https://somedomain.com/search", properties.getSearchUrl());
        assertEquals("username1", properties.getAuthUsername());
        assertEquals("password1", properties.getAuthPassword());
        assertEquals(1000, properties.getConnectionTimeout());
        assertEquals(2000, properties.getReadTimeout());
    }

    @Test
    public void shouldBuildToughJetIntegrationPropertiesWithDefaultConnectionTimeoutMsAndReadTimeoutMs() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.toughjet.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.toughjet.auth.username", "username1")
                .withProperty("integration.toughjet.auth.password", "password1");

        // When
        ToughJetIntegrationProperties properties = new ToughJetIntegrationPropertiesReader(environment).read();

        // Then
        assertEquals(3000, properties.getConnectionTimeout());
        assertEquals(3000, properties.getReadTimeout());
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfSearchUrlNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.toughjet.searchUrl is not defined.");

        // When
        new ToughJetIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfAuthUsernameNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.toughjet.searchUrl", "https://somedomain.com/search");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.toughjet.auth.username is not defined.");

        // When
        new ToughJetIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIllegalStateExceptionIfAuthPasswordNotSet() {

        // Given

        MockEnvironment environment = new MockEnvironment()
                .withProperty("integration.toughjet.searchUrl", "https://somedomain.com/search")
                .withProperty("integration.toughjet.auth.username", "username1");

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("integration.toughjet.auth.password is not defined.");

        // When
        new ToughJetIntegrationPropertiesReader(environment).read();

        // Then
        // Expect test to be passed.
    }
}