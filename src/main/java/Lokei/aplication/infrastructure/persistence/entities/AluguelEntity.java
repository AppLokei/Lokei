package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_aluguel")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AluguelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @CreationTimestamp
    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @CreationTimestamp
    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_aluguel")
    private StatusAluguelEnum statusAluguel;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDate datacriacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avaliacao_id")
    private AvaliacaoEntity avaliacao;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private AnuncioEntity anuncio;

}
