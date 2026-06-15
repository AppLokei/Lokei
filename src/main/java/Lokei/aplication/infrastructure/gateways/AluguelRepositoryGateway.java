package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AluguelMapper;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class AluguelRepositoryGateway implements AluguelGateway {

    private final AluguelRepository aluguelRepository;
    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;

    public AluguelRepositoryGateway(AluguelRepository aluguelRepository,
                                    AnuncioRepository anuncioRepository,
                                    UsuarioRepository usuarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.anuncioRepository = anuncioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Aluguel> buscarPorId(Long id) {
        return aluguelRepository.findById(id).map(AluguelMapper::toDomain);
    }

    @Override
    public boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId) {
        return aluguelRepository.existsAluguelEmAndamento(anuncioId);
    }

    @Override
    public Page<Aluguel> buscarAluguelPorUsuario(Long usuarioId, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return aluguelRepository.findByUsuarioId(usuarioId, pageable).map(AluguelMapper::toDomain);
    }

    @Override
    public List<Aluguel> buscarReservasPorAnuncioEStatus(Long anuncioId, Collection<StatusAluguelEnum> status) {
        return aluguelRepository
                .findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(anuncioId, status)
                .stream()
                .map(AluguelMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeReservaSobreposta(Long anuncioId, Collection<StatusAluguelEnum> status, Date dataInicio, Date dataFim) {
        return aluguelRepository.existsReservaSobreposta(anuncioId, status, dataInicio, dataFim);
    }

    @Override
    public Aluguel salvar(Aluguel aluguel, Long anuncioId, Long locatarioId) {
        AluguelEntity entity = AluguelMapper.toEntity(aluguel);
        entity.setDatacriacao(new Date());
        entity.setAnuncio(anuncioRepository.getReferenceById(anuncioId));
        entity.setLocatario(usuarioRepository.getReferenceById(locatarioId));

        AluguelEntity salvo = aluguelRepository.save(entity);
        return AluguelMapper.toDomain(salvo);
    }

    @Override
    public Aluguel atualizarStatus(Long id, StatusAluguelEnum novoStatus) {
        AluguelEntity entity = aluguelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel não encontrado."));

        entity.setStatusAluguel(novoStatus);
        AluguelEntity salvo = aluguelRepository.save(entity);
        return AluguelMapper.toDomain(salvo);
    }
}
