package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AnuncioMapper;
import Lokei.aplication.infrastructure.persistence.mapper.DenunciaMapper;
import Lokei.aplication.infrastructure.persistence.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DenunciaRepositoryGateway implements DenunciaGateway {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Override
    public Denuncia criarDenuncia(Denuncia denuncia) {
        DenunciaEntity entity = DenunciaMapper.toEntity(denuncia);
        DenunciaEntity salvo = denunciaRepository.save(entity);
        return DenunciaMapper.toDomain(salvo);
    }
}
