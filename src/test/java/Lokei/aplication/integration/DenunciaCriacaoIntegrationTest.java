package Lokei.aplication.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de integracao da funcionalidade "criar denuncia de anuncio".
 * Sobe o contexto Spring completo (perfil de teste com H2) e exercita o fluxo
 * HTTP real: registrar usuarios -> publicar anuncio -> denunciar -> validar
 * persistencia e visibilidade no painel admin.
 */
class DenunciaCriacaoIntegrationTest extends AbstractIntegrationTest {

    @Test
    void deveCriarDenunciaDeAnuncioEDisponibilizarParaModeracao() throws Exception {
        String tokenLocador = registrarUsuario("LOCADOR", "Senha1234");
        String tokenLocatario = registrarUsuario("LOCATARIO", "Senha1234");

        int imagemId = uploadImagem(tokenLocador, "furadeira.jpg").get("imagemId").asInt();
        int anuncioId = readJson(mockMvc.perform(post("/anuncios")
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "titulo", "Furadeira de Impacto",
                                "descricao", "Furadeira potente para concreto",
                                "valorDiario", 45.0,
                                "categoria", "FURADEIRAS_E_PARAFUSADEIRAS",
                                "imagemIds", List.of(imagemId)
                        ))))
                .andExpect(status().isCreated())
                .andReturn()).get("id").asInt();

        // Funcionalidade sob teste: criacao da denuncia.
        JsonNode denuncia = readJson(mockMvc.perform(post("/anuncios/{id}/denuncias", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "motivo", "ANUNCIO_FALSO",
                                "descricao", "A ferramenta entregue e diferente da anunciada",
                                "imagens", List.of("/arquivos/evidencia.png")
                        ))))
                .andExpect(status().isCreated())
                .andReturn());

        assertThat(denuncia.get("status").asText()).isEqualTo("EM_ANALISE");
        assertThat(denuncia.get("motivo").asText()).isEqualTo("ANUNCIO_FALSO");
        assertThat(denuncia.get("anuncioId").asInt()).isEqualTo(anuncioId);
        assertThat(denuncia.get("denunciante").asText()).isNotBlank();
        assertThat(denuncia.get("imagens").size()).isEqualTo(1);
        int denunciaId = denuncia.get("id").asInt();

        // A denuncia recem-criada deve aparecer no painel do administrador.
        String tokenAdmin = login("admin@lokei.local", "Admin1234");
        JsonNode lista = readJson(mockMvc.perform(get("/admin/denuncias")
                        .header("Authorization", bearer(tokenAdmin)))
                .andExpect(status().isOk())
                .andReturn());

        boolean contemDenunciaCriada = false;
        for (JsonNode item : lista) {
            if (item.get("id").asInt() == denunciaId) {
                contemDenunciaCriada = true;
                assertThat(item.get("status").asText()).isEqualTo("EM_ANALISE");
            }
        }
        assertThat(contemDenunciaCriada).isTrue();

        // Usuario comum nao pode acessar o painel de denuncias.
        mockMvc.perform(get("/admin/denuncias")
                        .header("Authorization", bearer(tokenLocatario)))
                .andExpect(status().isForbidden());
    }
}
