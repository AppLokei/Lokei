package Lokei.aplication.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integrations")
public record IntegrationProperties(
        String viacepBaseUrl,
        String cpfBaseUrl
) {
}
