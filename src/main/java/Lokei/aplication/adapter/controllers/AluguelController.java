package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.res.AluguelResponse;
import Lokei.aplication.adapter.mapper.AluguelControllerMapper;
import Lokei.aplication.application.usecases.aluguel.BuscarAluguelPorUsuarioUseCase;
import Lokei.aplication.domain.entities.Aluguel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AluguelController {

    private final BuscarAluguelPorUsuarioUseCase buscarAluguelPorUsuarioUseCase;

    public AluguelController(BuscarAluguelPorUsuarioUseCase buscarAluguelPorUsuarioUseCase) {
        this.buscarAluguelPorUsuarioUseCase = buscarAluguelPorUsuarioUseCase;
    }

    @GetMapping(value = "/alugueis-por-usuario")
    public ResponseEntity<Page<AluguelResponse>> buscarAlugueisPorUsuario(
            @RequestParam String identificador,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "12") int tamanho) {

        Page<Aluguel> alugueis = buscarAluguelPorUsuarioUseCase.buscarAluguelPorUsuario(identificador, pagina, tamanho);

        return ResponseEntity.ok(alugueis.map(AluguelControllerMapper::toResponse));
    }
}

