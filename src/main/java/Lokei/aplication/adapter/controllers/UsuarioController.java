package Lokei.aplication.adapter.controllers;


import Lokei.aplication.application.usecases.usuario.CadastroUseCase;
import Lokei.aplication.domain.exceptions.UsuarioException;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private CadastroUseCase serviceCad;


    @PostMapping(value = "/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody UsuarioEntity usuario, @RequestParam String data){

        try{
            String mensagem = serviceCad.cadastro(usuario, data);
            return ResponseEntity.ok(mensagem);
        }catch (UsuarioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}