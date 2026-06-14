package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.UsuarioInvalidoException;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String senha;

    public Usuario(Long id, String nome, String email, String cpf, String telefone, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Usuario() {

    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void logarUsuario(String email, String senha){
        if (!email.equals(this.email) || !senha.equals(this.senha)) {
            throw new UsuarioInvalidoException("Senha ou usuário Inválido");
        }
    }
}


