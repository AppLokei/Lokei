package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.SolicitarAluguelResponse;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class SolicitarAluguelUseCase {

    private static final Set<statusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            statusAluguelEnum.EM_APROVACAO,
            statusAluguelEnum.CONFIRMADO,
            statusAluguelEnum.ATIVO
    );

    private final AluguelRepository aluguelRepository;
    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitarAluguelUseCase(
            AluguelRepository aluguelRepository,
            AnuncioRepository anuncioRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.aluguelRepository = aluguelRepository;
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public SolicitarAluguelResponse executar(Integer anuncioId, SolicitarAluguelRequest request) {
        if (request == null || request.usuarioId() == null) {
            throw new RegraDeNegocioException("Usuário locatário é obrigatório para solicitar o aluguel.");
        }

        LocalDate dataInicio = parseData(request.dataInicio(), "Data de início inválida. Use o formato yyyy-MM-dd.");
        LocalDate dataFim = parseData(request.dataFim(), "Data de fim inválida. Use o formato yyyy-MM-dd.");

        validarDatas(dataInicio, dataFim);

        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        Usuario locatario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário locatário não encontrado."));

        validarAnuncioDisponivel(anuncio, locatario.getId());
        validarSobreposicao(anuncioId, dataInicio, dataFim);

        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        BigDecimal valorTotal = anuncio.getValorDiario().multiply(BigDecimal.valueOf(quantidadeDias));

        Aluguel aluguel = new Aluguel();
        aluguel.setAnuncio(anuncio);
        aluguel.setLocatario(locatario);
        aluguel.setDataInicio(dataInicio);
        aluguel.setDataFim(dataFim);
        aluguel.setDatacriacao(LocalDateTime.now());
        aluguel.setStatusAluguel(statusAluguelEnum.EM_APROVACAO);
        aluguel.setValorTotal(valorTotal);

        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);

        return new SolicitarAluguelResponse(
                aluguelSalvo.getId(),
                anuncio.getId(),
                locatario.getId(),
                aluguelSalvo.getStatusAluguel().name(),
                dataInicio.toString(),
                dataFim.toString(),
                quantidadeDias,
                anuncio.getValorDiario(),
                valorTotal,
                "Solicitação de aluguel criada com sucesso."
        );
    }

    private LocalDate parseData(String data, String mensagemErro) {
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new RegraDeNegocioException(mensagemErro);
        }
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate hoje = LocalDate.now();

        if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
            throw new RegraDeNegocioException("Datas anteriores ao dia atual não podem ser selecionadas.");
        }

        if (dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de fim deve ser igual ou posterior à data de início.");
        }

        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        if (quantidadeDias > 30) {
            throw new RegraDeNegocioException("O período máximo de aluguel é de 30 dias.");
        }
    }

    private void validarAnuncioDisponivel(Anuncio anuncio, Integer usuarioId) {
        if (anuncio.getStatus() != statusAnuncioEnum.ATIVO) {
            throw new RegraDeNegocioException("Este anúncio não está mais disponível no momento.");
        }

        if (anuncio.getProprietario() != null && usuarioId.equals(anuncio.getProprietario().getId())) {
            throw new RegraDeNegocioException("O proprietário não pode alugar o próprio anúncio.");
        }

        if (anuncio.getValorDiario() == null || anuncio.getValorDiario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O anúncio informado não possui valor diário válido para reserva.");
        }
    }

    private void validarSobreposicao(Integer anuncioId, LocalDate dataInicio, LocalDate dataFim) {
        boolean existeReservaSobreposta = aluguelRepository.existsReservaSobreposta(
                anuncioId,
                STATUS_QUE_BLOQUEIAM,
                dataInicio,
                dataFim
        );

        if (existeReservaSobreposta) {
            throw new RegraDeNegocioException("Já existe uma reserva para o período informado.");
        }
    }
}
