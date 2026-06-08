package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.avaliacao.AvaliacaoResponse;
import Lokei.aplication.application.dto.avaliacao.CriarAvaliacaoAnuncioRequest;
import Lokei.aplication.application.dto.avaliacao.CriarAvaliacaoPerfilRequest;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoAvaliacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AluguelRepository aluguelRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, AluguelRepository aluguelRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public AvaliacaoResponse avaliarAnuncio(Integer usuarioId, CriarAvaliacaoAnuncioRequest request) {
        Aluguel aluguel = buscarAluguelConcluido(request.aluguelId());
        if (!aluguel.getLocatario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Apenas o locatario pode avaliar o anuncio.");
        }
        if (avaliacaoRepository.existsByAluguel_IdAndTipoAndAutor_Id(aluguel.getId(), tipoAvaliacaoEnum.ANUNCIO, usuarioId)) {
            throw new RegraDeNegocioException("Este anuncio ja foi avaliado para este aluguel.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAluguel(aluguel);
        avaliacao.setAutor(aluguel.getLocatario());
        avaliacao.setAnuncio(aluguel.getAnuncio());
        avaliacao.setTipo(tipoAvaliacaoEnum.ANUNCIO);
        avaliacao.setNota(request.nota());
        avaliacao.setComentario(request.comentario().trim());
        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public AvaliacaoResponse avaliarContraparte(Integer usuarioId, CriarAvaliacaoPerfilRequest request) {
        Aluguel aluguel = buscarAluguelConcluido(request.aluguelId());

        Usuario autor;
        Usuario alvo;
        tipoAvaliacaoEnum tipo;
        if (aluguel.getLocatario().getId().equals(usuarioId)) {
            autor = aluguel.getLocatario();
            alvo = aluguel.getAnuncio().getProprietario();
            tipo = tipoAvaliacaoEnum.LOCADOR;
        } else if (aluguel.getAnuncio().getProprietario().getId().equals(usuarioId)) {
            autor = aluguel.getAnuncio().getProprietario();
            alvo = aluguel.getLocatario();
            tipo = tipoAvaliacaoEnum.LOCATARIO;
        } else {
            throw new AcessoNegadoException("Voce nao participou deste aluguel.");
        }

        if (avaliacaoRepository.existsByAluguel_IdAndTipoAndAutor_Id(aluguel.getId(), tipo, usuarioId)) {
            throw new RegraDeNegocioException("A contraparte ja foi avaliada para este aluguel.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAluguel(aluguel);
        avaliacao.setAutor(autor);
        avaliacao.setAlvoUsuario(alvo);
        avaliacao.setTipo(tipo);
        avaliacao.setNota(request.nota());
        avaliacao.setComentario(request.comentario().trim());
        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> listarAvaliacoesDoAnuncio(Integer anuncioId) {
        return avaliacaoRepository.findByAnuncio_IdAndTipoOrderByDataCriacaoDesc(anuncioId, tipoAvaliacaoEnum.ANUNCIO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> listarAvaliacoesDoUsuario(Integer usuarioId) {
        return java.util.stream.Stream.concat(
                        avaliacaoRepository.findByAlvoUsuario_IdAndTipoOrderByDataCriacaoDesc(usuarioId, tipoAvaliacaoEnum.LOCADOR).stream(),
                        avaliacaoRepository.findByAlvoUsuario_IdAndTipoOrderByDataCriacaoDesc(usuarioId, tipoAvaliacaoEnum.LOCATARIO).stream()
                )
                .sorted(java.util.Comparator.comparing(Avaliacao::getDataCriacao).reversed())
                .map(this::toResponse)
                .toList();
    }

    private Aluguel buscarAluguelConcluido(Integer aluguelId) {
        Aluguel aluguel = aluguelRepository.findDetailedById(aluguelId)
                .orElseThrow(() -> new RegraDeNegocioException("Aluguel nao encontrado."));
        if (aluguel.getStatusAluguel() != statusAluguelEnum.CONCLUIDO) {
            throw new RegraDeNegocioException("Avaliacao somente e permitida apos a conclusao do aluguel.");
        }
        return aluguel;
    }

    private AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getTipo().name(),
                avaliacao.getAutor() != null ? avaliacao.getAutor().getNome() : null,
                avaliacao.getAlvoUsuario() != null ? avaliacao.getAlvoUsuario().getNome() : null,
                DataFormatUtils.formatarDataHora(avaliacao.getDataCriacao())
        );
    }
}
