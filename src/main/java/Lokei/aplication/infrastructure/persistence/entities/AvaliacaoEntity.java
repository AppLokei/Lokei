package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "tb_avaliacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer nota;
    private String comentario;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @OneToOne
    @JoinColumn(name = "aluguel_id")
    private AluguelEntity aluguel;

}


