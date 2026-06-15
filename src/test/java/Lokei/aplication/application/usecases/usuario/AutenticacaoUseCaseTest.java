package Lokei.aplication.application.usecases.usuario;

import Lokei.aplication.adapter.dto.req.EsqueciSenhaRequest;
import Lokei.aplication.adapter.dto.req.LoginRequest;
import Lokei.aplication.adapter.dto.req.RedefinirSenhaRequest;
import Lokei.aplication.adapter.dto.res.AuthResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponseLog;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.infrastructure.config.SecurityProperties;
import Lokei.aplication.infrastructure.config.security.JwtService;
import Lokei.aplication.infrastructure.config.security.UsuarioAutenticado;
import Lokei.aplication.infrastructure.persistence.entities.TokenRecSenha;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.repository.TokenRecSenhaRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private CpfValidationUseCase cpfValidationService;

    @Mock
    private TokenRecSenhaRepository tokenRecuperacaoSenhaRepository;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private NotificacaoUseCase notificacaoService;

    @InjectMocks
    private AutenticacaoUseCase autenticacaoUseCase;

    private UsuarioEntity usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@email.com");
        usuario.setCpf("12345678900");
        usuario.setTelefone("11999999999");
        usuario.setSenha("Teste123@");
    }

    @Nested
    class LoginTest {

        @Test
        void deveRealizarLoginComSucesso() {
            UsuarioAutenticado principal = mock(UsuarioAutenticado.class);
            Authentication authentication = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(principal);
            when(principal.getUsuario()).thenReturn(usuario);
            when(jwtService.gerarToken(principal)).thenReturn("token-jwt");

            AuthResponse response = autenticacaoUseCase.login(new LoginRequest("teste@email.com", "Teste123@"));

            assertNotNull(response);
            assertEquals("token-jwt", response.token());
            assertEquals("Bearer", response.tipo());
        }

        @Test
        void naoDeveRealizarLoginComCredenciaisInvalidas() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            assertThrows(BadCredentialsException.class, () ->
                    autenticacaoUseCase.login(new LoginRequest("teste@email.com", "senhaErrada"))
            );
        }

        @Test
        void deveNormalizarEmailAoFazerLogin() {
            UsuarioAutenticado principal = mock(UsuarioAutenticado.class);
            Authentication authentication = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(principal);
            when(principal.getUsuario()).thenReturn(usuario);
            when(jwtService.gerarToken(principal)).thenReturn("token-jwt");

            autenticacaoUseCase.login(new LoginRequest("  TESTE@EMAIL.COM  ", "Teste123@"));

            verify(authenticationManager).authenticate(
                    argThat(auth -> auth.getPrincipal().equals("teste@email.com"))
            );
        }
    }

    @Nested
    class LogoutTest {

        @Test
        void deveRealizarLogoutComSucesso() {
            MensagemResponseLog response = autenticacaoUseCase.logout();

            assertNotNull(response);
            assertEquals("Logout realizado com sucesso.", response.mensagem());
        }
    }

    @Nested
    class EsqueciSenhaTest {

        @Test
        void deveEnviarTokenDeRecuperacaoQuandoEmailExiste() {
            SecurityProperties.PasswordReset passwordReset = mock(SecurityProperties.PasswordReset.class);
            when(passwordReset.expirationMinutes()).thenReturn(30L);
            when(securityProperties.passwordReset()).thenReturn(passwordReset);
            when(usuarioRepository.findByEmailIgnoreCase("teste@email.com"))
                    .thenReturn(Optional.of(usuario));

            MensagemResponseLog response = autenticacaoUseCase.esqueciSenha(
                    new EsqueciSenhaRequest("teste@email.com")
            );

            assertNotNull(response);
            assertEquals("Se o e-mail existir, uma instrucao de recuperacao foi registrada.", response.mensagem());
            verify(tokenRecuperacaoSenhaRepository).save(any(TokenRecSenha.class));
            verify(notificacaoService).notificar(any(), any(), any(), any());
        }

        @Test
        void deveRetornarMesmaMensagemQuandoEmailNaoExiste() {
            when(usuarioRepository.findByEmailIgnoreCase("naoexiste@email.com"))
                    .thenReturn(Optional.empty());

            MensagemResponseLog response = autenticacaoUseCase.esqueciSenha(
                    new EsqueciSenhaRequest("naoexiste@email.com")
            );

            assertNotNull(response);
            assertEquals("Se o e-mail existir, uma instrucao de recuperacao foi registrada.", response.mensagem());
            verify(tokenRecuperacaoSenhaRepository, never()).save(any());
        }
    }

    @Nested
    class RedefinirSenhaTest {

        @Test
        void deveRedefinirSenhaComSucesso() {
            TokenRecSenha token = criarTokenValido();

            when(tokenRecuperacaoSenhaRepository.findByToken("token-valido"))
                    .thenReturn(Optional.of(token));
            when(passwordEncoder.encode("NovaSenha123")).thenReturn("hashNovaSenha");

            MensagemResponseLog response = autenticacaoUseCase.redefinirSenha(
                    new RedefinirSenhaRequest("token-valido", "NovaSenha123")
            );

            assertNotNull(response);
            assertEquals("Senha redefinida com sucesso.", response.mensagem());
            assertTrue(token.isUsado());
            verify(passwordEncoder).encode("NovaSenha123");
        }

        @Test
        void naoDeveRedefinirSenhaComTokenExpirado() {
            TokenRecSenha token = criarTokenValido();
            token.setExpiracao(LocalDateTime.now().minusMinutes(1));

            when(tokenRecuperacaoSenhaRepository.findByToken("token-expirado"))
                    .thenReturn(Optional.of(token));

            assertThrows(RegraDeNegocioException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-expirado", "NovaSenha123")
                    )
            );
        }

        @Test
        void naoDeveRedefinirSenhaComTokenJaUtilizado() {
            TokenRecSenha token = criarTokenValido();
            token.setUsado(true);

            when(tokenRecuperacaoSenhaRepository.findByToken("token-usado"))
                    .thenReturn(Optional.of(token));

            assertThrows(RegraDeNegocioException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-usado", "NovaSenha123")
                    )
            );
        }

        @Test
        void naoDeveRedefinirSenhaComTokenInexistente() {
            when(tokenRecuperacaoSenhaRepository.findByToken("token-inexistente"))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-inexistente", "NovaSenha123")
                    )
            );
        }

        @Test
        void naoDeveRedefinirSenhaComSenhaInvalida() {
            assertThrows(RegraDeNegocioException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-valido", "fraca")
                    )
            );
        }

        @Test
        void naoDeveRedefinirSenhaComSenhaSemNumero() {
            assertThrows(RegraDeNegocioException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-valido", "SenhaSemNumero")
                    )
            );
        }

        @Test
        void naoDeveRedefinirSenhaComSenhaSemLetra() {
            assertThrows(RegraDeNegocioException.class, () ->
                    autenticacaoUseCase.redefinirSenha(
                            new RedefinirSenhaRequest("token-valido", "12345678")
                    )
            );
        }
    }

    private TokenRecSenha criarTokenValido() {
        TokenRecSenha token = new TokenRecSenha();
        token.setUsuario(usuario);
        token.setToken("token-valido");
        token.setExpiracao(LocalDateTime.now().plusMinutes(30));
        token.setUsado(false);
        return token;
    }
}