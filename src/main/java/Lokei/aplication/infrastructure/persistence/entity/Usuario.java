package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.exception.UsuarioException;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String senha;

    public Usuario() {
    }

    public Usuario(Integer id, String nome, String email, String cpf, String telefone, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void validacaoDadosUsuario() {

        String regexSenha = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        String regex = "\\d{11}";
        if (nome == null || nome.isBlank()) throw new UsuarioException("Campo obrigatório.");

        if (email == null || email.isBlank()) {
            throw new UsuarioException("Campo obrigatório.");
        } else if (!email.contains("@")) {
            throw new UsuarioException("Informe um e-mail válido");
        }

        if (cpf == null || cpf.isBlank()) {
            throw new UsuarioException("Campo obrigatório.");
        } else if (!cpf.matches(regex)) {
            throw new UsuarioException("Informe um CPF válido.");
        }

        if (senha == null || senha.isBlank()) {
            throw new UsuarioException("Campo obrigatório.");

        } else if (!senha.matches(regexSenha)) {
            throw new UsuarioException("A senha deve conter no mínimo 8 caracteres, incluindo letras, números e caracteres especiais.");

        }

        if (telefone == null || telefone.isBlank()) {
            throw new UsuarioException("Campo obrigatório.");
        } else if (!telefone.matches(regex)) {
            throw new UsuarioException("Informe um telefone válido");
        }

    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
