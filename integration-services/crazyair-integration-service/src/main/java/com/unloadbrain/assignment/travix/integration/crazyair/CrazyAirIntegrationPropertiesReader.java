package com.unloadbrain.assignment.travix.integration.crazyair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.env.Environment;

@Getter
@Builder
@AllArgsConstructor
public class CrazyAirIntegrationPropertiesReader {

    private static final String KEY_NOT_DEFINED_FORMAT = "%s is not defined.";

    private static final String ENV_KEY_SEARCH_URL = "integration.crazyair.searchUrl";
    private static final String ENV_KEY_AUTH_API_KEY = "integration.crazyair.auth.apiKey";
    private static final String ENV_KEY_AUTH_API_SECRET = "integration.crazyair.auth.apiSecret";
    private static final String ENV_KEY_CONNECTION_TIMEOUT = "integration.crazyair.resttemplate.connectionTimeoutMs";
    private static final String ENV_KEY_READ_TIMEOUT = "integration.crazyair.resttemplate.readTimeoutMs";

    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 3000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 3000;

    private final Environment environment;

    public CrazyAirIntegrationProperties read() {

        String searchUrl = environment.getProperty(ENV_KEY_SEARCH_URL);
        if (searchUrl == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_SEARCH_URL));
        }

        String authApiKey = environment.getProperty(ENV_KEY_AUTH_API_KEY);
        if (authApiKey == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_AUTH_API_KEY));
        }

        String authApiSecret = environment.getProperty(ENV_KEY_AUTH_API_SECRET);
        if (authApiSecret == null) {
            throw new IllegalStateException(String.format(KEY_NOT_DEFINED_FORMAT, ENV_KEY_AUTH_API_SECRET));
        }

        String connectionTimeoutString = environment.getProperty(ENV_KEY_CONNECTION_TIMEOUT);
        int connectionTimeout = connectionTimeoutString == null ?
                DEFAULT_CONNECTION_TIMEOUT_MS : Integer.parseInt(connectionTimeoutString);

        String readTimeoutString = environment.getProperty(ENV_KEY_READ_TIMEOUT);
        int readTimeout = readTimeoutString == null ? DEFAULT_READ_TIMEOUT_MS : Integer.parseInt(readTimeoutString);

        return CrazyAirIntegrationProperties.builder()
                .searchUrl(searchUrl)
                .authApiKey(authApiKey)
                .authApiSecret(authApiSecret)
                .connectionTimeout(connectionTimeout)
                .readTimeout(readTimeout)
                .build();
    }
}
