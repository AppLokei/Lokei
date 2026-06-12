package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.adapter.dto.CpfResponse;
import Lokei.aplication.application.useCases.AvaliarAnuncioUseCase;
import Lokei.aplication.application.useCases.CadastroUseCase;
import Lokei.aplication.infrastructure.exception.UsuarioException;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private CadastroUseCase serviceCad;


    @PostMapping(value = "/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody Usuario usuario,@RequestParam String data){

        try{
            String mensagem = serviceCad.cadastro(usuario, data);
            return ResponseEntity.ok(mensagem);
        }catch (UsuarioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}
