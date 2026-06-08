package Lokei.aplication.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void ensureSequence() {
        SEQUENCE.incrementAndGet();
    }

    protected String registrarUsuario(String papel, String senha) throws Exception {
        int seed = SEQUENCE.incrementAndGet();
        String email = "user" + seed + "@lokei.test";
        String cpf = gerarCpf(seed);
        String telefone = String.format("6199999%04d", seed % 10000);

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "nome", "Usuario " + seed,
                                "email", email,
                                "cpf", cpf,
                                "telefone", telefone,
                                "senha", senha,
                                "aceitouTermos", true,
                                "papel", papel,
                                "endereco", Map.of(
                                        "logradouro", "Rua Teste",
                                        "bairro", "Centro",
                                        "numero", "10",
                                        "complemento", "Apto 1",
                                        "cidade", "Brasilia",
                                        "estado", "DF",
                                        "cep", "70000000"
                                )
                        ))))
                .andExpect(status().isOk())
                .andReturn();

        return readJson(result).get("token").asText();
    }

    protected String login(String email, String senha) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "senha", senha))))
                .andExpect(status().isOk())
                .andReturn();
        return readJson(result).get("token").asText();
    }

    protected JsonNode uploadImagem(String token, String nome) throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files",
                nome,
                "image/jpeg",
                "fake-image-content".getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc.perform(multipart("/arquivos/upload")
                        .file(file)
                        .header("Authorization", bearer(token))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        return readJson(result).get(0);
    }

    protected JsonNode readJson(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }

    protected String gerarCpf(int seed) {
        String base = String.format("%09d", seed % 1_000_000_000);
        int d1 = digito(base, 10);
        int d2 = digito(base + d1, 11);
        return base + d1 + d2;
    }

    private int digito(String value, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < value.length(); i++) {
            soma += Character.getNumericValue(value.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
