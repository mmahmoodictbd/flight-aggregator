package com.unloadbrain.assignment.travix.integration.toughjet;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ToughJetIntegrationProperties {

    private final String searchUrl;

    private final String authUsername;

    private final String authPassword;

    private final int connectionTimeout;

    private final int readTimeout;

}
