package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.NotFoundException;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.ImagemEntity;
import Lokei.aplication.infrastructure.persistence.mapper.ImagemMapper;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.ImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImagemRepositoryGateway implements ImagemGateway {

    private final ImagemRepository imagemEntityRepository;
    private final AnuncioRepository anuncioRepository;

    @Override
    public Imagem salvarImagem(Imagem imagem, Long anuncioId) throws NotFoundException {
        AnuncioEntity anuncio = anuncioRepository.findById(anuncioId).orElseThrow(() -> new NotFoundException(anuncioId));
        ImagemEntity entity = ImagemMapper.toEntity(imagem, anuncio);
        ImagemEntity salvo = imagemEntityRepository.save(entity);
        return ImagemMapper.toDomain(salvo);
    }

    @Override
    public Optional<Imagem> buscarImagemPorId(Long id) {
        Optional<ImagemEntity> entity = imagemEntityRepository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }

        ImagemEntity imagemEntity = entity.get();
        return Optional.of(ImagemMapper.toDomain(imagemEntity));
    }

    @Transactional
    @Override
    public void apagarImagem(Long id) {
        imagemEntityRepository.deletarPorId(id);
    }


}
