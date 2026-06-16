package Lokei.aplication.integration;

import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvaliacaoDenunciaChatIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Test
    void devePermitirChatAvaliacoesDenunciaEModeracaoAdmin() throws Exception {
        String tokenLocador = registrarUsuario("LOCADOR", "Senha1234");
        String tokenLocatario = registrarUsuario("LOCATARIO", "Senha1234");
        String tokenAdmin = login("admin@lokei.local", "Admin1234");

        int imagemId = uploadImagem(tokenLocador, "serra.jpg").get("imagemId").asInt();
        int anuncioId = readJson(mockMvc.perform(post("/anuncios")
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "titulo", "Serra Circular",
                                "descricao", "Equipamento potente",
                                "valorDiario", 80.0,
                                "categoria", "SERRAS_E_MOTOSSERRAS",
                                "imagemIds", List.of(imagemId)
                        ))))
                .andExpect(status().isCreated())
                .andReturn()).get("id").asInt();

        LocalDate inicio = LocalDate.now().plusDays(6);
        LocalDate fim = inicio.plusDays(1);
        int aluguelId = readJson(mockMvc.perform(post("/anuncios/{id}/reservas", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "dataInicio", inicio.toString(),
                                "dataFim", fim.toString()
                        ))))
                .andExpect(status().isCreated())
                .andReturn()).get("aluguelId").asInt();

        mockMvc.perform(patch("/alugueis/{id}/aprovar", aluguelId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk());

        int chatId = readJson(mockMvc.perform(get("/alugueis/{id}/chat", aluguelId)
                        .header("Authorization", bearer(tokenLocatario)))
                .andExpect(status().isOk())
                .andReturn()).get("id").asInt();

        mockMvc.perform(post("/chats/{id}/mensagens", chatId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("conteudo", "Posso retirar amanha?"))))
                .andExpect(status().isOk());

        var notificacoesLocador = readJson(mockMvc.perform(get("/notificacoes")
                        .header("Authorization", bearer(tokenLocador)))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(notificacoesLocador.isArray()).isTrue();

        Aluguel aluguel = aluguelRepository.findById(aluguelId).orElseThrow();
        aluguel.setStatusAluguel(statusAluguelEnum.CONCLUIDO);
        aluguelRepository.save(aluguel);

        mockMvc.perform(post("/avaliacoes/anuncios")
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "aluguelId", aluguelId,
                                "nota", 5,
                                "comentario", "Excelente ferramenta"
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/avaliacoes/perfis")
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocador))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "aluguelId", aluguelId,
                                "nota", 5,
                                "comentario", "Locatario cuidadoso"
                        ))))
                .andExpect(status().isCreated());

        int denunciaId = readJson(mockMvc.perform(post("/anuncios/{id}/denuncias", anuncioId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenLocatario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "motivo", "ANUNCIO_FALSO",
                                "descricao", "Conteudo de teste para denuncia",
                                "imagens", List.of("/arquivos/anexo-teste.png")
                        ))))
                .andExpect(status().isCreated())
                .andReturn()).get("id").asInt();

        mockMvc.perform(patch("/admin/denuncias/{id}", denunciaId)
                        .with(csrf())
                        .header("Authorization", bearer(tokenAdmin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "aprovada", true,
                                "parecer", "Denuncia confirmada"
                        ))))
                .andExpect(status().isOk());

        var anuncioDetalhe = readJson(mockMvc.perform(get("/anuncios/{id}", anuncioId)
                        .header("Authorization", bearer(tokenLocatario)))
                .andExpect(status().isOk())
                .andReturn());
        assertThat(anuncioDetalhe.get("status").asText()).isEqualTo("DESATIVADO");
    }
}
