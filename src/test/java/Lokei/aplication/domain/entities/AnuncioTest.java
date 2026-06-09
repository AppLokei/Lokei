package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.AnuncioInvalidoException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnuncioTest {

    @Nested
    class AtualizarDadosTest {
        @Test
        void deveAtualizarDadosComSucesso() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);

            anuncio.atualizarDados("Furadeira", "Furadeira de impacto pronta para aluguel", BigDecimal.valueOf(30), List.of(imagem), novaFerramenta, false);

            assertEquals("Furadeira", anuncio.getTitulo());
            assertEquals("Furadeira de impacto pronta para aluguel", anuncio.getDescricao());
            assertEquals(BigDecimal.valueOf(30), anuncio.getValorDiario());
            assertEquals(novaFerramenta, anuncio.getFerramenta());
        }

        @Test
        void naoDevePermitirAlterarValorComAluguelEmAndamento() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.atualizarDados(
                            "Furadeira",
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.valueOf(30),
                            List.of(imagem),
                            novaFerramenta,
                            true
                    )
            );
        }

        @Test
        void devePermitirAtualizarDadosComAluguelEmAndamento() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);

            anuncio.atualizarDados(
                    "Furadeira",
                    "Furadeira de impacto pronta para aluguel",
                    BigDecimal.valueOf(20),
                    List.of(imagem),
                    novaFerramenta,
                    true
            );

            assertEquals("Furadeira", anuncio.getTitulo());
            assertEquals("Furadeira de impacto pronta para aluguel", anuncio.getDescricao());
            assertEquals(BigDecimal.valueOf(20), anuncio.getValorDiario());
            assertEquals(novaFerramenta, anuncio.getFerramenta());
        }

        @Test
        void naoDevePermitirAtualizarAnuncioComDadosInvalidos() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);


            AnuncioInvalidoException exception = assertThrows(
                    AnuncioInvalidoException.class,
                    () -> anuncio.atualizarDados(
                            null,
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.valueOf(20),
                            List.of(imagem),
                            novaFerramenta,
                            true
                    )
            );

            assertEquals("O título é obrigatório.", exception.getMessage());
        }

        @Test
        void naoDevePermitirAtualizarValorDoAnuncioParaZero() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);


            assertThrows(
                    AnuncioInvalidoException.class,
                    () -> anuncio.atualizarDados(
                            "Furadeira",
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.ZERO,
                            List.of(imagem),
                            novaFerramenta,
                            false
                    )
            );
        }

        @Test
        void naoDevePermitirAtualizarValorDoAnuncioParaNegativo() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();
            Imagem imagem = criarImagem(1L);

            assertThrows(
                    AnuncioInvalidoException.class,
                    () -> anuncio.atualizarDados(
                            "Furadeira",
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.valueOf(-10),
                            List.of(imagem),
                            novaFerramenta,
                            false
                    )
            );
        }

        @Test
        void naoDevePermitirAtualizarAnuncioDeixandoSemImagem() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();

            assertThrows(
                    AnuncioInvalidoException.class,
                    () -> anuncio.atualizarDados(
                            "Furadeira",
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.valueOf(20),
                            List.of(),
                            novaFerramenta,
                            false
                    )
            );
        }

        @Test
        void naoDevePermitirAtualizarAnuncioExcedindoOTamanhaoMaximoDeImagens() {
            Anuncio anuncio = criarAnuncio();
            Ferramenta novaFerramenta = criarFerramenta();

            List<Imagem> imagens = List.of(
                    criarImagem(1L),
                    criarImagem(2L),
                    criarImagem(3L),
                    criarImagem(4L),
                    criarImagem(5L),
                    criarImagem(6L)
            );

            assertThrows(
                    AnuncioInvalidoException.class,
                    () -> anuncio.atualizarDados(
                            "Furadeira",
                            "Furadeira de impacto pronta para aluguel",
                            BigDecimal.valueOf(20),
                            imagens,
                            novaFerramenta,
                            false
                    )
            );
        }
    }

    @Nested
    class PausarAnuncioTest {
        @Test
        void devePausarAnuncioComSucesso() {
            Anuncio anuncio = criarAnuncio();

            anuncio.pausarAnuncio(false);
            assertEquals(StatusAnuncioEnum.PAUSADO, anuncio.getStatus());
        }

        @Test
        void naoDevePausarAnuncioPausado() {
            Anuncio anuncio = criarAnuncio();

            anuncio.pausarAnuncio(false);

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.pausarAnuncio(false));
        }

        @Test
        void naoDevePausarAnuncioDesativado() {
            Anuncio anuncio = criarAnuncio();

            anuncio.desativarAnuncio(false);

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.pausarAnuncio(false));
        }

        @Test
        void naoDevePausarAnuncioComAluguelEmAndamento() {
            Anuncio anuncio = criarAnuncio();

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.pausarAnuncio(true));
        }
    }

    @Nested
    class ReativarAnuncioTest {
        @Test
        void deveReativarAnuncioComSucesso() {
            Anuncio anuncio = criarAnuncio();

            anuncio.pausarAnuncio(false);
            anuncio.reativarAnuncio();

            assertEquals(StatusAnuncioEnum.ATIVO, anuncio.getStatus());
        }

        @Test
        void naoDeveReativarAnuncioComStatusAtivo() {
            Anuncio anuncio = criarAnuncio();

            assertThrows(AnuncioInvalidoException.class, anuncio::reativarAnuncio);
        }

        @Test
        void naoDeveReativarAnuncioDesativado() {
            Anuncio anuncio = criarAnuncio();

            anuncio.desativarAnuncio(false);

            assertThrows(AnuncioInvalidoException.class, anuncio::reativarAnuncio);
        }
    }

    @Nested
    class DesativarAnuncioTest {
        @Test
        void deveDesativarAnuncioComSucesso() {
            Anuncio anuncio = criarAnuncio();

            anuncio.desativarAnuncio(false);

            assertEquals(StatusAnuncioEnum.DESATIVADO, anuncio.getStatus());
        }

        @Test
        void naoDeveDesativarAnuncioComAluguelEmAndamento() {
            Anuncio anuncio = criarAnuncio();

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.desativarAnuncio(true));
        }

        @Test
        void naoDeveDesativarAnuncioComStatusDesativado() {
            Anuncio anuncio = criarAnuncio();

            anuncio.desativarAnuncio(false);

            assertThrows(AnuncioInvalidoException.class, () -> anuncio.desativarAnuncio(false));
        }
    }

    @Nested
    class AdicionarImagemTest {
        @Test
        void deveAdicionarImagemComSucesso() {
            Anuncio anuncio = criarAnuncio();

            Imagem novaImagem = new Imagem(2L, "https://teste.com/imagem2.jpg", "publicId2");

            anuncio.adicionarImagem(novaImagem);

            assertEquals(2, anuncio.getImagens().size());
            assertTrue(anuncio.getImagens().contains(novaImagem));
        }

        @Test
        void naoDevePermitirAdicionarMaisDeCincoImagens() {
            Anuncio anuncio = criarAnuncioComCincoImagens();

            Imagem novaImagem = new Imagem(6L, "url6", "id6");

            AnuncioInvalidoException exception = assertThrows(AnuncioInvalidoException.class, () -> anuncio.adicionarImagem(novaImagem));

            assertEquals("Limite máximo de 5 fotos atingido.", exception.getMessage());
        }
    }

    @Nested
    class RemoverImagemTest {
        @Test
        void deveRemoverImagemComSucesso() {
            Anuncio anuncio = criarAnuncio();

            Imagem novaImagem = new Imagem(2L, "https://teste.com/imagem2.jpg", "publicId2");
            anuncio.adicionarImagem(novaImagem);

            anuncio.removerImagem(novaImagem);

            assertEquals(1, anuncio.getImagens().size());
        }

        @Test
        void naoDevePermitirRemoverAUltimaImagem() {
            Anuncio anuncio = criarAnuncio();

            Imagem imagem = anuncio.getImagens().get(0);

            AnuncioInvalidoException exception = assertThrows(AnuncioInvalidoException.class, () -> anuncio.removerImagem(imagem));

            assertEquals("O anúncio deve ter ao menos uma foto.", exception.getMessage());
        }
    }


    private Anuncio criarAnuncio() {
        Ferramenta ferramenta = new Ferramenta(null, "Martelo", CategoriaEnum.MARTELOS);

        Imagem imagem = new Imagem(null, "https://teste.com/imagem.jpg", "publicId");

        return new Anuncio(
                null,
                "Martelo",
                "Martelo pronto para aluguel",
                BigDecimal.valueOf(20),
                StatusAnuncioEnum.ATIVO,
                ferramenta,
                List.of(imagem),
                1L
        );
    }

    private Anuncio criarAnuncioComCincoImagens() {

        Ferramenta ferramenta = new Ferramenta(null, "Martelo", CategoriaEnum.MARTELOS);

        List<Imagem> imagens = List.of(
                new Imagem(1L, "url1", "id1"),
                new Imagem(2L, "url2", "id2"),
                new Imagem(3L, "url3", "id3"),
                new Imagem(4L, "url4", "id4"),
                new Imagem(5L, "url5", "id5")
        );

        return new Anuncio(
                null,
                "Martelo",
                "Martelo pronto para aluguel",
                BigDecimal.valueOf(20),
                StatusAnuncioEnum.ATIVO,
                ferramenta,
                imagens,
                1L
        );
    }

    private Ferramenta criarFerramenta() {
        return new Ferramenta(1L, "Martelo", CategoriaEnum.FURADEIRAS_E_PARAFUSADEIRAS);
    }

    private Imagem criarImagem(Long id) {
        return new Imagem(id, "url" + id, "publicId" + id);
    }
}