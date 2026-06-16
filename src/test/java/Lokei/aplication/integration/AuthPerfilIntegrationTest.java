package Lokei.aplication.integration;

import Lokei.aplication.infrastructure.persistence.repository.TokenRecuperacaoSenhaRepository;
import Lokei.aplication.infrastructure.persistence.repository.TokenVerificacaoEmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthPerfilIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TokenVerificacaoEmailRepository tokenVerificacaoEmailRepository;

    @Autowired
    private TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;

    @Test
    void deveCadastrarAtualizarPerfilAlterarEmailERedefinirSenha() throws Exception {
        String senhaInicial = "Senha1234";
        String token = registrarUsuario("LOCATARIO", senhaInicial);

        var perfil = readJson(mockMvc.perform(get("/perfil")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn());
        String emailAtual = perfil.get("email").asText();
        assertThat(perfil.get("cpf").asText()).hasSize(11);

        mockMvc.perform(put("/perfil")
                        .with(csrf())
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "nome", "Usuario Atualizado",
                                "telefone", "61912345678",
                                "endereco", Map.of(
                                        "logradouro", "Rua Nova",
                                        "bairro", "Asa Sul",
                                        "numero", "20",
                                        "complemento", "Casa",
                                        "cidade", "Brasilia",
                                        "estado", "DF",
                                        "cep", "70000000"
                                )
                        ))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/perfil/email/solicitar")
                        .with(csrf())
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("novoEmail", "novo." + emailAtual))))
                .andExpect(status().isOk());

        String tokenEmail = tokenVerificacaoEmailRepository.findAll().stream()
                .reduce((first, second) -> second)
                .orElseThrow()
                .getToken();

        mockMvc.perform(post("/perfil/email/confirmar")
                        .with(csrf())
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("token", tokenEmail))))
                .andExpect(status().isOk());

        String tokenAtualizado = login("novo." + emailAtual, senhaInicial);
        String novoEmail = readJson(mockMvc.perform(get("/perfil")
                        .header("Authorization", bearer(tokenAtualizado)))
                .andExpect(status().isOk())
                .andReturn()).get("email").asText();
        assertThat(novoEmail).isEqualTo("novo." + emailAtual);

        mockMvc.perform(post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", novoEmail))))
                .andExpect(status().isOk());

        String tokenRecuperacao = tokenRecuperacaoSenhaRepository.findAll().stream()
                .reduce((first, second) -> second)
                .orElseThrow()
                .getToken();

        String novaSenha = "NovaSenha123";
        mockMvc.perform(post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("token", tokenRecuperacao, "novaSenha", novaSenha))))
                .andExpect(status().isOk());

        String novoToken = login(novoEmail, novaSenha);
        assertThat(novoToken).isNotBlank();
    }
}
