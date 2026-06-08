package Lokei.aplication.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AnuncioReservaFlowIntegrationTest extends AbstractIntegrationTest {

    @Test
    void deveCriarAnuncioBuscarReservarAprovarECancelar() throws Exception {
        String tokenLocador = registrarUsuario("LOCADOR", "Senha1234");
        String tokenLocatario = registrarUsuario("LOCATARIO", "Senha1234");

        int imagemId = uploadImagem(tokenLocador, "furadeira.jpg").get("imagemId").asInt();
        var anuncioJson = readJson(mockMvc.perform(post("/anuncios")
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "titulo", "Furadeira Bosch",
                                "descricao", "Ferramenta para testes integrados",
                                "valorDiario", 50.0,
                                "categoria", "FURADEIRAS_E_PARAFUSADEIRAS",
                                "imagemIds", List.of(imagemId)
                        ))))
                .andExpect(status().isCreated())
                .andReturn());
        int anuncioId = anuncioJson.get("id").asInt();

        mockMvc.perform(patch("/anuncios/{id}/pausar", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/anuncios/{id}/reativar", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk());

        var busca = readJson(mockMvc.perform(get("/anuncios").param("q", "Bosch"))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(busca.get("itens").get(0).get("titulo").asText()).isEqualTo("Furadeira Bosch");

        var detalhe = readJson(mockMvc.perform(get("/anuncios/{id}", anuncioId)
                        .header("Authorization", bearer(tokenLocatario)))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(detalhe.get("acaoPrimaria").asText()).isEqualTo("SOLICITAR_ALUGUEL");

        LocalDate inicio = LocalDate.now().plusDays(5);
        LocalDate fim = inicio.plusDays(2);
        var aluguelJson = readJson(mockMvc.perform(post("/anuncios/{id}/reservas", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "dataInicio", inicio.toString(),
                                "dataFim", fim.toString()
                        ))))
                .andExpect(status().isCreated())
                .andReturn());
        int aluguelId = aluguelJson.get("aluguelId").asInt();
        assertThat(aluguelJson.get("status").asText()).isEqualTo("EM_APROVACAO");

        var recebidos = readJson(mockMvc.perform(get("/alugueis/recebidos")
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(recebidos.get(0).get("id").asInt()).isEqualTo(aluguelId);

        mockMvc.perform(patch("/alugueis/{id}/aprovar", aluguelId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk());

        var detalheAluguel = readJson(mockMvc.perform(get("/alugueis/{id}", aluguelId)
                        .header("Authorization", bearer(tokenLocatario)))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(detalheAluguel.get("status").asText()).isEqualTo("CONFIRMADO");

        mockMvc.perform(patch("/alugueis/{id}/cancelar", aluguelId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("motivo", "Mudanca de planos"))))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/anuncios/{id}", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isBadRequest());
    }
}
