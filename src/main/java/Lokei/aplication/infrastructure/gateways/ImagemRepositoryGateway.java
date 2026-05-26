package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.ImagemEntity;
import Lokei.aplication.infrastructure.persistence.mapper.ImagemMapper;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.ImagemRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ImagemRepositoryGateway implements ImagemGateway {

    private final ImagemRepository imagemEntityRepository;
    private final AnuncioRepository anuncioRepository;

    public ImagemRepositoryGateway(ImagemRepository imagemEntityRepository, AnuncioRepository anuncioRepository) {
        this.imagemEntityRepository = imagemEntityRepository;
        this.anuncioRepository = anuncioRepository;
    }

    @Override
    public Imagem salvarImagem(Imagem imagem, Long anuncioId) {
        AnuncioEntity anuncio = anuncioRepository.findById(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));
        ImagemEntity entity = ImagemMapper.toEntity(imagem, anuncio);
        ImagemEntity salvo = imagemEntityRepository.save(entity);
        return ImagemMapper.toDomain(salvo);
    }

    @Override
    public List<Imagem> buscarImagensPorAnuncio(Long anuncioId) {
        AnuncioEntity anuncio = anuncioRepository.findById(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));

        List<ImagemEntity> entities = imagemEntityRepository.findImagemEntitiesByAnuncio(anuncio);
        List<Imagem> imagens = new ArrayList<>();
        for(ImagemEntity entity : entities) {
            imagens.add(ImagemMapper.toDomain(entity));
        }

        return imagens;
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
