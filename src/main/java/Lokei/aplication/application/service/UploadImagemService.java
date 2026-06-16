package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.upload.ImagemUploadResponse;
import Lokei.aplication.infrastructure.config.StorageProperties;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.repository.ImagemRepository;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadImagemService {

    private static final Set<String> CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final int MAX_FILES = 5;

    private final ImagemRepository imagemRepository;
    private final Path storageDir;

    public UploadImagemService(ImagemRepository imagemRepository, StorageProperties storageProperties) {
        this.imagemRepository = imagemRepository;
        this.storageDir = Path.of(storageProperties.imageDir()).toAbsolutePath().normalize();
    }

    @Transactional
    public List<ImagemUploadResponse> upload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new RegraDeNegocioException("Envie ao menos uma imagem.");
        }
        if (files.size() > MAX_FILES) {
            throw new RegraDeNegocioException("Cada anuncio pode ter no maximo 5 fotos.");
        }

        criarDiretorioSeNecessario();

        return files.stream().map(this::salvarArquivo).toList();
    }

    @Transactional(readOnly = true)
    public Resource carregarArquivo(String filename) {
        try {
            Path file = storageDir.resolve(filename).normalize();
            if (!file.startsWith(storageDir)) {
                throw new RegraDeNegocioException("Arquivo invalido.");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) {
                throw new RecursoNaoEncontradoException("Arquivo nao encontrado.");
            }
            return resource;
        } catch (IOException exception) {
            throw new RecursoNaoEncontradoException("Arquivo nao encontrado.");
        }
    }

    @Transactional(readOnly = true)
    public Set<Imagem> resolverImagensParaAnuncio(List<Integer> imagemIds, Anuncio anuncioAtual) {
        if (imagemIds == null || imagemIds.isEmpty()) {
            throw new RegraDeNegocioException("O anuncio deve possuir ao menos uma foto.");
        }
        if (imagemIds.size() > MAX_FILES) {
            throw new RegraDeNegocioException("Cada anuncio pode ter no maximo 5 fotos.");
        }

        Set<Imagem> imagens = new LinkedHashSet<>();
        for (int indice = 0; indice < imagemIds.size(); indice++) {
            Integer imagemId = imagemIds.get(indice);
            Imagem imagem = imagemRepository.findById(imagemId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Imagem nao encontrada: " + imagemId));

            if (imagem.getAnuncio() != null && (anuncioAtual == null || !imagem.getAnuncio().getId().equals(anuncioAtual.getId()))) {
                throw new RegraDeNegocioException("Uma das imagens informadas ja pertence a outro anuncio.");
            }

            imagem.setOrdem(indice);
            imagens.add(imagem);
        }
        return imagens;
    }

    private ImagemUploadResponse salvarArquivo(MultipartFile file) {
        validar(file);
        try {
            String extension = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;
            Path destino = storageDir.resolve(filename);
            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Imagem imagem = new Imagem();
            imagem.setNomeArquivo(file.getOriginalFilename());
            imagem.setImagemUrl("/arquivos/" + filename);
            imagem.setContentType(file.getContentType());
            imagem.setTamanhoBytes(file.getSize());
            imagem.setOrdem(0);
            Imagem salva = imagemRepository.save(imagem);

            return new ImagemUploadResponse(salva.getId(), salva.getNomeArquivo(), salva.getImagemUrl(), salva.getContentType(), salva.getTamanhoBytes());
        } catch (IOException exception) {
            throw new RegraDeNegocioException("Falha ao armazenar a imagem enviada.");
        }
    }

    private void validar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RegraDeNegocioException("Envie uma imagem valida.");
        }
        if (!CONTENT_TYPES.contains(file.getContentType())) {
            throw new RegraDeNegocioException("Somente arquivos JPG, JPEG e PNG sao aceitos.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RegraDeNegocioException("Cada imagem enviada nao pode ultrapassar 5 MB.");
        }
    }

    private void criarDiretorioSeNecessario() {
        try {
            Files.createDirectories(storageDir);
        } catch (IOException exception) {
            throw new RegraDeNegocioException("Nao foi possivel preparar o diretorio de upload.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".bin";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
