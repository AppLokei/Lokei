package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.CategoriaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_ferramenta")
@Getter
@Setter
@NoArgsConstructor
public class FerramentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    @OneToOne(mappedBy = "ferramenta")
    private AnuncioEntity anuncio;

    public FerramentaEntity(Long id, String nome, CategoriaEnum categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }
}
