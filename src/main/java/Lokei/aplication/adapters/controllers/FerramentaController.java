package Lokei.aplication.adapters.controllers;

import Lokei.aplication.adapters.dtos.req.CriarFerramentaRequest;
import Lokei.aplication.adapters.dtos.res.CriarFerramentaResponse;
import Lokei.aplication.adapters.mapper.FerramentaDTOMapper;
import Lokei.aplication.application.usecases.ferramenta.CriarFerramentaUseCase;
import Lokei.aplication.domain.entities.Ferramenta;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ferramentas")
public class FerramentaController {

    private final CriarFerramentaUseCase criarFerramentaUseCase;

    public FerramentaController(CriarFerramentaUseCase criarFerramentaUseCase, FerramentaDTOMapper ferramentaDTOMapper) {
        this.criarFerramentaUseCase = criarFerramentaUseCase;
    }

    @PostMapping
    CriarFerramentaResponse criar(@RequestBody CriarFerramentaRequest request) {
        Ferramenta ferramenta = FerramentaDTOMapper.toFerramenta(request);
        Ferramenta salvo = criarFerramentaUseCase.execute(ferramenta);
        return FerramentaDTOMapper.toResponse(salvo);
    }

}
