package Lokei.aplication.adapter.controllers;

import Lokei.aplication.application.usecases.AvaliarAnuncioUseCase;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliarAnuncioUseCase avaliarUseCase;

    @PostMapping
    public ResponseEntity<String> avaliarAnuncio(@RequestBody AvaliacaoEntity avaliacaoEntity){
       String mensagem = avaliarUseCase.avaliarAnuncio(avaliacaoEntity);
        return ResponseEntity.ok().body(mensagem);
    }

}
