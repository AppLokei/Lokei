package Lokei.aplication.adapters.controllers;

import Lokei.aplication.adapters.dtos.AnuncioRequestDTO;
import Lokei.aplication.adapters.dtos.AnuncioResponseDTO;
import Lokei.aplication.adapters.mapper.AnuncioControllerMapper;
import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/anuncios")
public class AnuncioController {

    private final CriarAnuncioUseCase criarAnuncioUseCase;
    private final AtualizarAnuncioUseCase atualizarAnuncioUseCase;
    private final ExcluirAnuncioUseCase excluirAnuncioUseCase;
    private final BuscarTodosAnunciosUseCase buscarTodosAnunciosUseCase;
    private final BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase;
    private final BuscarAnunciosPorCategoriaUseCase buscarAnunciosPorCategoriaUseCase;

    public AnuncioController(CriarAnuncioUseCase criarAnuncioUseCase, AtualizarAnuncioUseCase atualizarAnuncioUseCase, ExcluirAnuncioUseCase excluirAnuncioUseCase, BuscarTodosAnunciosUseCase buscarTodosAnunciosUseCase, BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase, BuscarAnunciosPorCategoriaUseCase buscarAnunciosPorCategoriaUseCase) {
        this.criarAnuncioUseCase = criarAnuncioUseCase;
        this.atualizarAnuncioUseCase = atualizarAnuncioUseCase;
        this.excluirAnuncioUseCase = excluirAnuncioUseCase;
        this.buscarTodosAnunciosUseCase = buscarTodosAnunciosUseCase;
        this.buscarAnuncioPorIdUseCase = buscarAnuncioPorIdUseCase;
        this.buscarAnunciosPorCategoriaUseCase = buscarAnunciosPorCategoriaUseCase;
    }

    @PostMapping
    public ResponseEntity<AnuncioResponseDTO> criar(@RequestBody @Valid AnuncioRequestDTO dto) {
        Anuncio anuncio = AnuncioControllerMapper.toAnuncio(dto);
        Anuncio criado = criarAnuncioUseCase.execute(anuncio);
        AnuncioResponseDTO response = AnuncioControllerMapper.toResponseDTO(criado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AnuncioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AnuncioRequestDTO dto) {
        Anuncio anuncio = AnuncioControllerMapper.toAnuncio(dto);
        anuncio.setId(id);
        Anuncio atualizado = atualizarAnuncioUseCase.execute(anuncio);
        AnuncioResponseDTO response = AnuncioControllerMapper.toResponseDTO(atualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        excluirAnuncioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AnuncioResponseDTO>> buscarTodos() {
        List<Anuncio> anuncios = buscarTodosAnunciosUseCase.execute();
        List<AnuncioResponseDTO> response = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            response.add(AnuncioControllerMapper.toResponseDTO(anuncio));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AnuncioResponseDTO> findById(@PathVariable Long id) {
        Anuncio anuncio = buscarAnuncioPorIdUseCase.execute(id);
        return ResponseEntity.ok(AnuncioControllerMapper.toResponseDTO(anuncio));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<AnuncioResponseDTO>> buscarPorCategoria(@PathVariable CategoriaEnum categoria) {
        List<Anuncio> anuncios = buscarAnunciosPorCategoriaUseCase.execute(categoria);
        List<AnuncioResponseDTO> response = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            response.add(AnuncioControllerMapper.toResponseDTO(anuncio));
        }
        return ResponseEntity.ok(response);
    }
}
