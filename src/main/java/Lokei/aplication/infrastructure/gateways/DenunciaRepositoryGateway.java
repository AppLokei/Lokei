package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import Lokei.aplication.infrastructure.persistence.mapper.DenunciaMapper;
import Lokei.aplication.infrastructure.persistence.repository.DenunciaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DenunciaRepositoryGateway implements DenunciaGateway {

    private final DenunciaRepository denunciaRepository;

    public DenunciaRepositoryGateway(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    @Override
    public Denuncia criarDenuncia(Denuncia denuncia) {
        DenunciaEntity entity = DenunciaMapper.toEntity(denuncia);
        DenunciaEntity salvo = denunciaRepository.save(entity);
        return DenunciaMapper.toDomain(salvo);
    }

    @Override
    public Denuncia atualizarDenuncia(Denuncia denuncia) {
        DenunciaEntity entity = DenunciaMapper.toEntity(denuncia);
        DenunciaEntity salvo = denunciaRepository.save(entity);
        return DenunciaMapper.toDomain(salvo);
    }

    @Override
    public Optional<Denuncia> buscarDenunciaPorId(Long id) {
        return denunciaRepository.findById(id).map(DenunciaMapper::toDomain);
    }

    @Override
    public List<Denuncia> buscarDenunciasPorAnuncio(Long anuncioId) {
        return denunciaRepository.findByAnuncio_Id(anuncioId)
                .stream().map(DenunciaMapper::toDomain).toList();
    }

    @Override
    public List<Denuncia> buscarDenunciasPorStatus(StatusDenunciaEnum status) {
        return denunciaRepository.findByStatus(status)
                .stream().map(DenunciaMapper::toDomain).toList();
    }

    @Override
    public boolean existeDenunciaPendentePorAnuncioEDenunciante(Long anuncioId, Long denuncianteId) {
        return denunciaRepository.existsByAnuncio_IdAndDenunciante_IdAndStatus(
                anuncioId, denuncianteId, StatusDenunciaEnum.PENDENTE);
    }
}