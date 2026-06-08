package Lokei.aplication.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_usuario")
public class UsuarioEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String senha;
    private boolean ativo;

    public UsuarioEntity(){
    }

    public UsuarioEntity(Long id, String nome, String email, String cpf, String telefone, String senha, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity usuarioEntity = (UsuarioEntity) o;
        return Objects.equals(id, usuarioEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public JsonSubTypes.Type getPapel() {

    }

    public boolean isAtivo() {
    }
}
