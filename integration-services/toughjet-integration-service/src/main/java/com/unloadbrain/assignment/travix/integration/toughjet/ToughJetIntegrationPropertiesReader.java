package com.unloadbrain.assignment.travix.integration.toughjet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.env.Environment;

@Getter
@Builder
@AllArgsConstructor
public class ToughJetIntegrationPropertiesReader {

    private static final String KEY_NOT_DEFINED_FORMAT = "%s is not defined.";

    private static final String ENV_KEY_SEARCH_URL = "integration.toughjet.searchUrl";
    private static final String ENV_KEY_AUTH_USERNAME = "integration.toughjet.auth.username";
    private static final String ENV_KEY_AUTH_PASSWORD = "integration.toughjet.auth.password";
    private static final String ENV_KEY_CONNECTION_TIMEOUT = "integration.toughjet.resttemplate.connectionTimeoutMs";
    private static final String ENV_KEY_READ_TIMEOUT = "integration.toughjet.resttemplate.readTimeoutMs";

    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 3000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 3000;

    private final Environment environment;

    public ToughJetIntegrationProperties read() {

        String searchUrl = environment.getProperty(ENV_KEY_SEARCH_URL);
        if (searchUrl == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_SEARCH_URL));
        }

        String authUsername = environment.getProperty(ENV_KEY_AUTH_USERNAME);
        if (authUsername == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_AUTH_USERNAME));
        }

        String authPassword = environment.getProperty(ENV_KEY_AUTH_PASSWORD);
        if (authPassword == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_AUTH_PASSWORD));
        }

        String connectionTimeoutString = environment.getProperty(ENV_KEY_CONNECTION_TIMEOUT);
        int connectionTimeout = connectionTimeoutString == null ?
                DEFAULT_CONNECTION_TIMEOUT_MS : Integer.parseInt(connectionTimeoutString);

        String readTimeoutString = environment.getProperty(ENV_KEY_READ_TIMEOUT);
        int readTimeout = readTimeoutString == null ? DEFAULT_READ_TIMEOUT_MS : Integer.parseInt(readTimeoutString);

        return ToughJetIntegrationProperties.builder()
                .searchUrl(searchUrl)
                .authUsername(authUsername)
                .authPassword(authPassword)
                .connectionTimeout(connectionTimeout)
                .readTimeout(readTimeout)
                .build();
    }
}
