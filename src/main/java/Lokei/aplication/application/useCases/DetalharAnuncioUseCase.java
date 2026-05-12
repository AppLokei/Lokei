package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.AnuncioDetalheResponse;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class DetalharAnuncioUseCase {

    private static final String ACAO_LOGIN = "FAZER_LOGIN";
    private static final String ACAO_EDITAR = "EDITAR_ANUNCIO";
    private static final String ACAO_RESERVAR = "SOLICITAR_ALUGUEL";
    private static final String ACAO_INDISPONIVEL = "INDISPONIVEL";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final AnuncioRepository anuncioRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public DetalharAnuncioUseCase(
            AnuncioRepository anuncioRepository,
            AvaliacaoRepository avaliacaoRepository
    ) {
        this.anuncioRepository = anuncioRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public AnuncioDetalheResponse executar(Integer anuncioId, Integer usuarioId) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAluguel_Anuncio_IdOrderByDataCriacaoDesc(anuncioId);
        List<AnuncioDetalheResponse.AvaliacaoResumo> avaliacoesResponse = avaliacoes.stream()
                .sorted(Comparator.comparing(Avaliacao::getDataCriacao, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(avaliacao -> new AnuncioDetalheResponse.AvaliacaoResumo(
                        avaliacao.getId(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        formatarDataHora(avaliacao.getDataCriacao())
                ))
                .toList();

        BigDecimal notaMedia = avaliacoes.stream()
                .map(Avaliacao::getNota)
                .filter(nota -> nota != null)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!avaliacoes.isEmpty()) {
            notaMedia = notaMedia.divide(BigDecimal.valueOf(avaliacoes.size()), 2, RoundingMode.HALF_UP);
        } else {
            notaMedia = BigDecimal.ZERO;
        }

        return new AnuncioDetalheResponse(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getCategoria() == null ? null : anuncio.getCategoria().name(),
                anuncio.getStatus() == null ? null : anuncio.getStatus().name(),
                montarProprietario(anuncio.getProprietario()),
                anuncio.getImagens().stream()
                        .sorted(Comparator.comparing(Imagem::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(Imagem::getImagemUrl)
                        .toList(),
                avaliacoesResponse,
                notaMedia,
                avaliacoesResponse.size(),
                determinarAcaoPrimaria(anuncio, usuarioId),
                podeReservar(anuncio, usuarioId)
        );
    }

    private AnuncioDetalheResponse.ProprietarioResumo montarProprietario(Usuario proprietario) {
        if (proprietario == null) {
            return null;
        }

        return new AnuncioDetalheResponse.ProprietarioResumo(
                proprietario.getId(),
                proprietario.getNome(),
                mascararEmail(proprietario.getEmail()),
                mascararTelefone(proprietario.getTelefone())
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

    private String formatarDataHora(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return data.format(DATE_TIME_FORMAT);
    }
}
