package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.exceptions.FerramentaInvalidaException;

import java.util.Objects;

public class Ferramenta {

    private Long id;
    private String nome;
    private CategoriaEnum categoria;

    public Ferramenta(){}

    public Ferramenta(Long id, String nome, CategoriaEnum categoria) {

        if (nome == null || nome.isBlank()){
            throw new FerramentaInvalidaException("O nome da ferramenta é obrigatório.");
        }
        if (categoria == null) {
            throw new FerramentaInvalidaException("Selecione uma categoria para a ferramenta.");
        }

        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
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

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ferramenta that = (Ferramenta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
