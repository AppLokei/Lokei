package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.gateways.FerramentaGateway;
import Lokei.aplication.infrastructure.persistence.entities.FerramentaEntity;
import Lokei.aplication.infrastructure.persistence.mapper.FerramentaMapper;
import Lokei.aplication.infrastructure.persistence.repository.FerramentaRepository;
import org.springframework.stereotype.Component;

@Component
public class FerramentaRepositoryGateway implements FerramentaGateway {

    private final FerramentaRepository ferramentaRepository;

    public FerramentaRepositoryGateway(FerramentaRepository ferramentaRepository) {
        this.ferramentaRepository = ferramentaRepository;
    }

    @Override
    public Ferramenta criarFerramenta(Ferramenta ferramenta) {
        FerramentaEntity ferramentaEntity = FerramentaMapper.toEntity(ferramenta);
        FerramentaEntity salvo = ferramentaRepository.save(ferramentaEntity);
        return FerramentaMapper.toDomain(salvo);
    }

    @Override
    public Ferramenta atualizarFerramenta(Ferramenta ferramenta) {
        FerramentaEntity ferramentaEntity = FerramentaMapper.toEntity(ferramenta);
        FerramentaEntity salvo = ferramentaRepository.save(ferramentaEntity);
        return FerramentaMapper.toDomain(salvo);
    }
}
