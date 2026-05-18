package Lokei.aplication.adapters.controllers;

import Lokei.aplication.adapters.dtos.req.AtualizarAnuncioRequest;
import Lokei.aplication.adapters.dtos.req.CriarAnuncioRequest;
import Lokei.aplication.adapters.dtos.res.AnuncioResponseDTO;
import Lokei.aplication.adapters.mapper.AnuncioControllerMapper;
import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.infrastructure.config.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AnuncioController {

    private final CriarAnuncioUseCase criarAnuncioUseCase;
    private final AtualizarAnuncioUseCase atualizarAnuncioUseCase;
    private final ExcluirAnuncioUseCase excluirAnuncioUseCase;
    private final BuscarTodosAnunciosUseCase buscarTodosAnunciosUseCase;
    private final BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase;
    private final BuscarAnunciosPorCategoriaUseCase buscarAnunciosPorCategoriaUseCase;
    private final CloudinaryService cloudinaryService;

    public AnuncioController(CriarAnuncioUseCase criarAnuncioUseCase, AtualizarAnuncioUseCase atualizarAnuncioUseCase, ExcluirAnuncioUseCase excluirAnuncioUseCase, BuscarTodosAnunciosUseCase buscarTodosAnunciosUseCase, BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase, BuscarAnunciosPorCategoriaUseCase buscarAnunciosPorCategoriaUseCase, CloudinaryService cloudinaryService) {
        this.criarAnuncioUseCase = criarAnuncioUseCase;
        this.atualizarAnuncioUseCase = atualizarAnuncioUseCase;
        this.excluirAnuncioUseCase = excluirAnuncioUseCase;
        this.buscarTodosAnunciosUseCase = buscarTodosAnunciosUseCase;
        this.buscarAnuncioPorIdUseCase = buscarAnuncioPorIdUseCase;
        this.buscarAnunciosPorCategoriaUseCase = buscarAnunciosPorCategoriaUseCase;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/anuncio")
    public ResponseEntity<AnuncioResponseDTO> criar(@RequestPart("dados") @Valid CriarAnuncioRequest dto, @RequestPart("imagens") List<MultipartFile> imagens) {

        List<String> imagensUrls = new ArrayList<>();
        for (MultipartFile imagem : imagens) {
            String url = cloudinaryService.uploadImagem(imagem);
            imagensUrls.add(url);
        }

        Anuncio anuncio = AnuncioControllerMapper.toAnuncio(dto);

        Anuncio criado = criarAnuncioUseCase.execute(anuncio, imagensUrls);

        AnuncioResponseDTO response = AnuncioControllerMapper.toResponseDTO(criado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/anuncio/{id}")
    public ResponseEntity<AnuncioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarAnuncioRequest dto) {
        Anuncio anuncio = AnuncioControllerMapper.toAnuncioUpdate(dto);
        anuncio.setId(id);
        Anuncio atualizado = atualizarAnuncioUseCase.execute(id, anuncio);
        AnuncioResponseDTO response = AnuncioControllerMapper.toResponseDTO(atualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/anuncio/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        excluirAnuncioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/anuncios")
    public ResponseEntity<List<AnuncioResponseDTO>> buscarTodos() {
        List<Anuncio> anuncios = buscarTodosAnunciosUseCase.execute();
        List<AnuncioResponseDTO> response = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            response.add(AnuncioControllerMapper.toResponseDTO(anuncio));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/anuncio/{id}")
    public ResponseEntity<AnuncioResponseDTO> buscarPorId(@PathVariable Long id) {
        Anuncio anuncio = buscarAnuncioPorIdUseCase.execute(id);
        return ResponseEntity.ok(AnuncioControllerMapper.toResponseDTO(anuncio));
    }

    @GetMapping("/anuncios/categorias/{categoria}")
    public ResponseEntity<List<AnuncioResponseDTO>> buscarPorCategoria(@PathVariable CategoriaEnum categoria) {
        List<Anuncio> anuncios = buscarAnunciosPorCategoriaUseCase.execute(categoria);
        List<AnuncioResponseDTO> response = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            response.add(AnuncioControllerMapper.toResponseDTO(anuncio));
        }
        return ResponseEntity.ok(response);
    }
}
