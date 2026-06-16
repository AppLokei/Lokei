package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.profile.ConsultaCepResponse;
import Lokei.aplication.infrastructure.config.IntegrationProperties;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CepService {

    private final IntegrationProperties integrationProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CepService(IntegrationProperties integrationProperties, ObjectMapper objectMapper) {
        this.integrationProperties = integrationProperties;
        this.objectMapper = objectMapper;
    }

    public ConsultaCepResponse consultar(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new RegraDeNegocioException("CEP deve conter 8 digitos.");
        }

        String url = integrationProperties.viacepBaseUrl();
        String endereco = (url.endsWith("/") ? url : url + "/") + cep + "/json/";

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RegraDeNegocioException("Nao foi possivel consultar o CEP informado.");
            }

            JsonNode root = objectMapper.readTree(response.body());
            if (root.has("erro") && root.get("erro").asBoolean()) {
                throw new RegraDeNegocioException("CEP nao encontrado.");
            }

            return new ConsultaCepResponse(
                    cep,
                    root.path("logradouro").asText(),
                    root.path("bairro").asText(),
                    root.path("localidade").asText(),
                    root.path("uf").asText()
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RegraDeNegocioException("Consulta de CEP interrompida.");
        } catch (IOException | IllegalArgumentException exception) {
            throw new RegraDeNegocioException("Nao foi possivel consultar o CEP informado.");
        }
    }
}
