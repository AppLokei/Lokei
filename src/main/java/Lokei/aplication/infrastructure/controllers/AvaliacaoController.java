package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.useCases.AvaliarAnuncioUseCase;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliarAnuncioUseCase avaliarUseCase;

    @PostMapping
    public ResponseEntity<String> avaliarAnuncio(@RequestBody Avaliacao avaliacao) {
        String mensagem = avaliarUseCase.avaliarAnuncio(avaliacao);
        return ResponseEntity.ok().body(mensagem);
    }

    @PostMapping(value = "/teste")
    public ResponseEntity<Aluguel> teste(@RequestBody Aluguel aluguel) {
        return ResponseEntity.ok().body(avaliarUseCase.teste(aluguel));
    }

}
