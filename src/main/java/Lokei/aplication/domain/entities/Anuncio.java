package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.AnuncioInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Anuncio {
    private final Long id;
    private String titulo;
    private String descricao;
    private BigDecimal valorDiario;
    private StatusAnuncioEnum status;
    private final LocalDateTime dataCriacao;

    private List<Imagem> imagens = new ArrayList<>();
    private Ferramenta ferramenta;
    private final Long usuarioId;

    public Anuncio(Long id, String titulo, String descricao, BigDecimal valorDiario, StatusAnuncioEnum status, Ferramenta ferramenta, List<Imagem> imagens, Long usuarioId) {
        validaDados(titulo, descricao, valorDiario, imagens);

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = status != null ? status : StatusAnuncioEnum.ATIVO;
        this.dataCriacao = LocalDateTime.now();
        this.ferramenta = ferramenta;
        this.imagens = new ArrayList<>(imagens);
        this.usuarioId = usuarioId;
    }

    public void atualizarDados(String titulo, String descricao, BigDecimal valorDiario, List<Imagem> imagens, Ferramenta ferramenta, boolean aluguelEmAndamento) {
        validaDados(titulo, descricao, valorDiario, imagens);

        if (aluguelEmAndamento) {
            if (!valorDiario.equals(this.valorDiario)) {
                throw new AnuncioInvalidoException("Não é possível alterar o valor diário enquanto houver um aluguel em andamento.");
            }
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.ferramenta = ferramenta;
        this.imagens = new ArrayList<>(imagens);
    }

    public void pausarAnuncio(boolean aluguelEmAndamento) {
        if (aluguelEmAndamento) {
            throw new AnuncioInvalidoException("Não é possivel pausar um anuncio com alugueis em andamento");
        }
        if (this.status == StatusAnuncioEnum.PAUSADO) {
            throw new AnuncioInvalidoException("O Anúncio já está pausado.");
        }
        if (this.status == StatusAnuncioEnum.DESATIVADO) {
            throw new AnuncioInvalidoException("Não é possível pausar um anúncio desativado.");
        }
        this.status = StatusAnuncioEnum.PAUSADO;
    }

    public void reativarAnuncio() {
        if (status.equals(StatusAnuncioEnum.PAUSADO)) {
            this.status = StatusAnuncioEnum.ATIVO;
        } else {
            throw new AnuncioInvalidoException("Somente anuncios pausados podem ser reativados");
        }
    }

    public void desativarAnuncio(boolean aluguelEmAndamento) {
        if (aluguelEmAndamento) {
            throw new AnuncioInvalidoException("Não é possivel desativar um anuncio com alugueis em andamento");
        }
        if (this.status == StatusAnuncioEnum.DESATIVADO) {
            throw new AnuncioInvalidoException("O anúncio já está desativado.");
        }
        this.status = StatusAnuncioEnum.DESATIVADO;
    }

    private void validaDados(String titulo, String descricao, BigDecimal valorDiario, List<Imagem> imagens) {
        if (titulo == null || titulo.isBlank()) throw new AnuncioInvalidoException("O título é obrigatório.");
        if (descricao == null || descricao.isBlank()) throw new AnuncioInvalidoException("A descrição é obrigatória");
        if (valorDiario.signum() <= 0) throw new AnuncioInvalidoException("O valor deve ser maior que R$ 0,00.");
        if (imagens == null || imagens.isEmpty()) throw new AnuncioInvalidoException("O anúncio deve ter ao menos uma foto.");
        if (imagens.size() > 5) throw new AnuncioInvalidoException("Limite máximo de 5 fotos atingido.");
    }

    public void adicionarImagem(Imagem imagem) {
        if (this.imagens.size() >= 5) {
            throw new AnuncioInvalidoException("Limite máximo de 5 fotos atingido.");
        }
        this.imagens.add(imagem);
    }

    public void removerImagem(Imagem imagem) {
        if (this.imagens.size() <= 1) {
            throw new AnuncioInvalidoException("O anúncio deve ter ao menos uma foto.");
        }
        this.imagens.remove(imagem);
    }

    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public BigDecimal getValorDiario() {
        return valorDiario;
    }
    public StatusAnuncioEnum getStatus() {
        return status;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public List<Imagem> getImagens() {
        return new ArrayList<>(imagens);
    }
    public Ferramenta getFerramenta() {
        return ferramenta;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(id, anuncio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}