package Lokei.aplication.infrastructure.persistence.entities;


import Lokei.aplication.domain.exceptions.UsuarioException;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;



    @OneToMany(mappedBy = "usuario")
    private Set<AnuncioEntity> anuncios = new HashSet<>();

    public UsuarioEntity(){
    }

    public UsuarioEntity(Long id, String nome, String email, String cpf, String telefone, String senha, EnderecoEntity endereco, Set<AnuncioEntity> anuncios) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
        this.endereco = endereco;
        this.anuncios = anuncios;
    }

    public EnderecoEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoEntity endereco) {
        this.endereco = endereco;
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

    public Set<AnuncioEntity> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(Set<AnuncioEntity> anuncios) {
        this.anuncios = anuncios;
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
        UsuarioEntity usuarioEntity = (UsuarioEntity) o;
        return Objects.equals(id, usuarioEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
