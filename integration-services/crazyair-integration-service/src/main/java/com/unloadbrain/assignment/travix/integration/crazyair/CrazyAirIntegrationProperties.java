package com.unloadbrain.assignment.travix.integration.crazyair;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrazyAirIntegrationProperties {

    private final String searchUrl;

    private final String authApiKey;

    private final String authApiSecret;

    private final int connectionTimeout;

    private final int readTimeout;

}
