package Lokei.aplication.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        Jwt jwt,
        PasswordReset passwordReset
) {
    public record Jwt(
            String secret,
            long expirationMinutes
    ) {
    }

    public record PasswordReset(
            long expirationMinutes
    ) {
    }
}
