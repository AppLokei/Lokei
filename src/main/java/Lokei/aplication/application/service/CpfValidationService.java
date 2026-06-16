package Lokei.aplication.application.service;

import Lokei.aplication.infrastructure.config.IntegrationProperties;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CpfValidationService {

    private final IntegrationProperties integrationProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CpfValidationService(IntegrationProperties integrationProperties, ObjectMapper objectMapper) {
        this.integrationProperties = integrationProperties;
        this.objectMapper = objectMapper;
    }

    public boolean cpfRegular(String cpf) {
        if (!cpfValido(cpf)) {
            return false;
        }

        String baseUrl = integrationProperties.cpfBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            return true;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl.endsWith("/") ? baseUrl + cpf : baseUrl + "/" + cpf))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode root = objectMapper.readTree(response.body());
                String situacao = null;
                if (root.hasNonNull("situacao")) {
                    situacao = root.get("situacao").asText();
                } else if (root.hasNonNull("status")) {
                    situacao = root.get("status").asText();
                }
                return situacao == null || "regular".equalsIgnoreCase(situacao);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (IOException | IllegalArgumentException ignored) {
        }

        return true;
    }

    public boolean cpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }

        return digito(cpf, 10) == Character.getNumericValue(cpf.charAt(9))
                && digito(cpf, 11) == Character.getNumericValue(cpf.charAt(10));
    }

    private int digito(String cpf, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < pesoInicial - 1; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
