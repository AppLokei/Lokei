package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.anuncio.*;
import Lokei.aplication.application.dto.common.PageResponse;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.categoriaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;
    private final UploadImagemService uploadImagemService;
    private final AluguelRepository aluguelRepository;

    public AnuncioService(
            AnuncioRepository anuncioRepository,
            UsuarioRepository usuarioRepository,
            UploadImagemService uploadImagemService,
            AluguelRepository aluguelRepository
    ) {
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
        this.uploadImagemService = uploadImagemService;
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public AnuncioResumoResponse criar(Integer usuarioId, CriarAnuncioRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(request.titulo().trim());
        anuncio.setDescricao(request.descricao().trim());
        anuncio.setValorDiario(request.valorDiario());
        anuncio.setCategoria(parseCategoria(request.categoria()));
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
        anuncio.setProprietario(usuario);
        sincronizarImagens(anuncio, request.imagemIds());

        return toResumo(anuncioRepository.save(anuncio));
    }

    @Transactional
    public AnuncioResumoResponse atualizar(Integer anuncioId, Integer usuarioId, AtualizarAnuncioRequest request) {
        Anuncio anuncio = buscarAnuncioDetalhado(anuncioId);
        validarProprietario(anuncio, usuarioId);

        if (aluguelRepository.existsByAnuncio_IdAndStatusAluguel(anuncioId, statusAluguelEnum.ATIVO)
                && anuncio.getValorDiario().compareTo(request.valorDiario()) != 0) {
            throw new RegraDeNegocioException("O valor da diaria nao pode ser alterado enquanto houver aluguel ativo.");
        }

        anuncio.setTitulo(request.titulo().trim());
        anuncio.setDescricao(request.descricao().trim());
        anuncio.setValorDiario(request.valorDiario());
        anuncio.setCategoria(parseCategoria(request.categoria()));
        sincronizarImagens(anuncio, request.imagemIds());
        return toResumo(anuncio);
    }

    @Transactional
    public void pausar(Integer anuncioId, Integer usuarioId) {
        Anuncio anuncio = buscarAnuncioDetalhado(anuncioId);
        validarProprietario(anuncio, usuarioId);
        if (aluguelRepository.existsByAnuncio_IdAndStatusAluguel(anuncioId, statusAluguelEnum.ATIVO)) {
            throw new RegraDeNegocioException("Nao e permitido pausar um anuncio que possua aluguel em andamento.");
        }
        anuncio.setStatus(statusAnuncioEnum.PAUSADO);
    }

    @Transactional
    public void reativar(Integer anuncioId, Integer usuarioId) {
        Anuncio anuncio = buscarAnuncioDetalhado(anuncioId);
        validarProprietario(anuncio, usuarioId);
        if (anuncio.getStatus() == statusAnuncioEnum.DESATIVADO) {
            throw new RegraDeNegocioException("Anuncios desativados pela moderacao nao podem ser reativados pelo locador.");
        }
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
    }

    @Transactional
    public void excluir(Integer anuncioId, Integer usuarioId) {
        Anuncio anuncio = buscarAnuncioDetalhado(anuncioId);
        validarProprietario(anuncio, usuarioId);
        if (aluguelRepository.existsByAnuncio_IdAndStatusAluguel(anuncioId, statusAluguelEnum.ATIVO)) {
            throw new RegraDeNegocioException("Nao e permitido excluir um anuncio com aluguel ativo.");
        }
        if (aluguelRepository.existsByAnuncio_Id(anuncioId)) {
            anuncio.setStatus(statusAnuncioEnum.DESATIVADO);
            anuncioRepository.save(anuncio);
        } else {
            anuncioRepository.delete(anuncio);
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<AnuncioResumoResponse> buscarCatalogo(
            String termo,
            String categoria,
            BigDecimal precoMin,
            BigDecimal precoMax,
            String cidade,
            int pagina,
            int tamanho,
            String ordenacao
    ) {
        Pageable pageable = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamanho, 1), 50), resolverOrdenacao(ordenacao));
        Page<Anuncio> result = anuncioRepository.buscarCatalogo(
                termo == null || termo.isBlank() ? null : termo.trim(),
                categoria == null || categoria.isBlank() ? null : parseCategoria(categoria),
                precoMin,
                precoMax,
                cidade == null || cidade.isBlank() ? null : cidade.trim(),
                pageable
        );
        return new PageResponse<>(
                result.getContent().stream().map(this::toResumo).toList(),
                result.getTotalElements(),
                result.getNumber(),
                result.getSize(),
                result.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public List<AnuncioResumoResponse> principais(int limite) {
        return anuncioRepository.buscarPrincipais(PageRequest.of(0, Math.min(Math.max(limite, 1), 20)))
                .stream()
                .limit(limite)
                .map(this::toResumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnuncioResumoResponse> meusAnuncios(Integer usuarioId) {
        return anuncioRepository.findByProprietario_IdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .filter(a -> a.getStatus() != statusAnuncioEnum.DESATIVADO)
                .map(this::toResumo)
                .toList();
    }

    @Transactional
    public void desativarPorDenuncia(Anuncio anuncio) {
        anuncio.setStatus(statusAnuncioEnum.DESATIVADO);
    }

    @Transactional(readOnly = true)
    public Anuncio buscarAnuncioDetalhado(Integer anuncioId) {
        return anuncioRepository.findDetailedById(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anuncio nao encontrado."));
    }

    private void validarProprietario(Anuncio anuncio, Integer usuarioId) {
        if (!anuncio.getProprietario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Apenas o proprietario pode alterar este anuncio.");
        }
    }

    private categoriaEnum parseCategoria(String categoria) {
        try {
            return categoriaEnum.valueOf(categoria.trim().toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            throw new RegraDeNegocioException("Categoria invalida.");
        }
    }

    private Sort resolverOrdenacao(String ordenacao) {
        if (ordenacao == null || ordenacao.isBlank() || "recente".equalsIgnoreCase(ordenacao)) {
            return Sort.by(Sort.Direction.DESC, "dataCriacao");
        }
        if ("precoAsc".equalsIgnoreCase(ordenacao)) {
            return Sort.by(Sort.Direction.ASC, "valorDiario");
        }
        if ("precoDesc".equalsIgnoreCase(ordenacao)) {
            return Sort.by(Sort.Direction.DESC, "valorDiario");
        }
        return Sort.by(Sort.Direction.DESC, "dataCriacao");
    }

    private void sincronizarImagens(Anuncio anuncio, List<Integer> imagemIds) {
        Set<Imagem> imagens = uploadImagemService.resolverImagensParaAnuncio(imagemIds, anuncio.getId() == null ? null : anuncio);
        anuncio.getImagens().clear();
        Set<Imagem> novasImagens = new LinkedHashSet<>();
        for (Imagem imagem : imagens) {
            imagem.setAnuncio(anuncio);
            novasImagens.add(imagem);
        }
        anuncio.getImagens().addAll(novasImagens);
    }

    private AnuncioResumoResponse toResumo(Anuncio anuncio) {
        String cidade = anuncio.getProprietario() != null && anuncio.getProprietario().getEndereco() != null
                ? anuncio.getProprietario().getEndereco().getCidade()
                : null;
        String descricaoCurta = anuncio.getDescricao().length() <= 120
                ? anuncio.getDescricao()
                : anuncio.getDescricao().substring(0, 117) + "...";
        String imagemPrincipal = anuncio.getImagens().stream()
                .sorted(java.util.Comparator.comparingInt(Imagem::getOrdem).thenComparing(Imagem::getId))
                .map(Imagem::getImagemUrl)
                .findFirst()
                .orElse(null);
        return new AnuncioResumoResponse(
                anuncio.getId(),
                anuncio.getTitulo(),
                descricaoCurta,
                anuncio.getValorDiario(),
                anuncio.getCategoria().name(),
                anuncio.getStatus().name(),
                cidade,
                imagemPrincipal
        );
    }
}
