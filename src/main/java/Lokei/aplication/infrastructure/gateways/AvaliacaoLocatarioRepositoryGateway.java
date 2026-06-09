package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.AvaliacaoLocatario;
import Lokei.aplication.domain.gateways.AvaliacaoLocatarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoLocatarioEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AvaliacaoLocatarioMapper;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoLocatarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoLocatarioRepositoryGateway implements AvaliacaoLocatarioGateway {

    private final AvaliacaoLocatarioRepository repository;

    public AvaliacaoLocatarioRepositoryGateway(AvaliacaoLocatarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public AvaliacaoLocatario salvar(AvaliacaoLocatario avaliacao) {
        AvaliacaoLocatarioEntity entity = AvaliacaoLocatarioMapper.toEntity(avaliacao);
        AvaliacaoLocatarioEntity savedEntity = repository.save(entity);
        return AvaliacaoLocatarioMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existeAvaliacaoParaAluguel(Long aluguelId, Long avaliadorId) {
        return repository.existsByAluguelIdAndAvaliadorId(aluguelId, avaliadorId);
    }

    @Override
    public Page<AvaliacaoLocatario> buscarPorAvaliado(Long avaliadoId, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return repository.findByAvaliadoId(avaliadoId, pageable)
                .map(AvaliacaoLocatarioMapper::toDomain);
    }
}
