package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.anuncio.AnuncioDetalheResponse;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoAvaliacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import Lokei.aplication.application.service.AnuncioService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class DetalharAnuncioUseCase {

    private static final String ACAO_LOGIN = "FAZER_LOGIN";
    private static final String ACAO_EDITAR = "EDITAR_ANUNCIO";
    private static final String ACAO_RESERVAR = "SOLICITAR_ALUGUEL";
    private static final String ACAO_INDISPONIVEL = "INDISPONIVEL";

    private final AnuncioService anuncioService;
    private final AvaliacaoRepository avaliacaoRepository;

    public DetalharAnuncioUseCase(AnuncioService anuncioService, AvaliacaoRepository avaliacaoRepository) {
        this.anuncioService = anuncioService;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public AnuncioDetalheResponse executar(Integer anuncioId, Integer usuarioId) {
        Anuncio anuncio = anuncioService.buscarAnuncioDetalhado(anuncioId);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAnuncio_IdAndTipoOrderByDataCriacaoDesc(anuncioId, tipoAvaliacaoEnum.ANUNCIO);

        List<AnuncioDetalheResponse.AvaliacaoResumo> avaliacoesResponse = avaliacoes.stream()
                .sorted(Comparator.comparing(Avaliacao::getDataCriacao, Comparator.reverseOrder()))
                .map(avaliacao -> new AnuncioDetalheResponse.AvaliacaoResumo(
                        avaliacao.getId(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getAutor() != null ? avaliacao.getAutor().getNome() : null,
                        DataFormatUtils.formatarDataHora(avaliacao.getDataCriacao())
                ))
                .toList();

        BigDecimal soma = avaliacoes.stream()
                .map(Avaliacao::getNota)
                .filter(nota -> nota != null)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal media = avaliacoes.isEmpty()
                ? BigDecimal.ZERO.setScale(2)
                : soma.divide(BigDecimal.valueOf(avaliacoes.size()), 2, RoundingMode.HALF_UP);

        return new AnuncioDetalheResponse(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getCategoria().name(),
                anuncio.getStatus().name(),
                toProprietario(anuncio.getProprietario()),
                anuncio.getImagens().stream()
                        .sorted(Comparator.comparingInt(Imagem::getOrdem).thenComparing(Imagem::getId))
                        .map(Imagem::getId)
                        .toList(),
                anuncio.getImagens().stream()
                        .sorted(Comparator.comparingInt(Imagem::getOrdem).thenComparing(Imagem::getId))
                        .map(Imagem::getImagemUrl)
                        .toList(),
                avaliacoesResponse,
                media,
                avaliacoesResponse.size(),
                determinarAcaoPrimaria(anuncio, usuarioId),
                podeReservar(anuncio, usuarioId)
        );
    }

    private AnuncioDetalheResponse.ProprietarioResumo toProprietario(Usuario proprietario) {
        if (proprietario == null) {
            return null;
        }
        String cidade = proprietario.getEndereco() != null ? proprietario.getEndereco().getCidade() : null;
        String cep = proprietario.getEndereco() != null ? proprietario.getEndereco().getCep() : null;
        String estado = proprietario.getEndereco() != null ? proprietario.getEndereco().getEstado() : null;
        return new AnuncioDetalheResponse.ProprietarioResumo(
                proprietario.getId(),
                proprietario.getNome(),
                mascararEmail(proprietario.getEmail()),
                mascararTelefone(proprietario.getTelefone()),
                cep,
                cidade,
                estado
        );
    }

    private String determinarAcaoPrimaria(Anuncio anuncio, Integer usuarioId) {
        if (usuarioId == null) {
            return ACAO_LOGIN;
        }
        if (anuncio.getProprietario() != null && usuarioId.equals(anuncio.getProprietario().getId())) {
            return ACAO_EDITAR;
        }
        if (anuncio.getStatus() != statusAnuncioEnum.ATIVO) {
            return ACAO_INDISPONIVEL;
        }
        return ACAO_RESERVAR;
    }

    private boolean podeReservar(Anuncio anuncio, Integer usuarioId) {
        return ACAO_RESERVAR.equals(determinarAcaoPrimaria(anuncio, usuarioId));
    }

    private String mascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] partes = email.split("@", 2);
        String inicio = partes[0].isEmpty() ? "*" : partes[0].substring(0, 1);
        return inicio + "***@" + partes[1];
    }

    private String mascararTelefone(String telefone) {
        if (telefone == null || telefone.length() < 4) {
            return telefone;
        }
        return "***-***-" + telefone.substring(telefone.length() - 4);
    }
}
