package com.medic.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value="medic")
@Data
public class MedicConfig {

    @JsonProperty("secret_key")
    private String secretKey;

    @JsonProperty("api_key")
    private String apiKey;

    private String loginURL;

    private String diagnosisURL;

    private String symptomsURL;
}
