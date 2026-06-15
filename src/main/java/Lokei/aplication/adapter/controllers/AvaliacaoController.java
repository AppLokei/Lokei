package Lokei.aplication.adapter.controllers;

import Lokei.aplication.application.usecases.avaliacao.AvaliarAnuncioUseCase;
import Lokei.aplication.application.usecases.avaliacao.ListarAvaliacaoAnuncioUseCase;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliarAnuncioUseCase avaliarUseCase;

    @Autowired
    private ListarAvaliacaoAnuncioUseCase listarAvaliacaoAnuncioUseCase;

    @PostMapping
    public ResponseEntity<String> avaliarAnuncio(@RequestBody AvaliacaoEntity avaliacaoEntity){
       String mensagem = avaliarUseCase.avaliarAnuncio(avaliacaoEntity);
        return ResponseEntity.ok().body(mensagem);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<List<AvaliacaoEntity>> listarAvaliacoes (@PathVariable Long id){
        List<AvaliacaoEntity> lista = listarAvaliacaoAnuncioUseCase.listarAvaliacoes(id);
        return ResponseEntity.ok().body(lista);
    }

}
