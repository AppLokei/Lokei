package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_anuncio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnuncioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "valor_diario", nullable = false)
    private BigDecimal valorDiario;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusAnuncioEnum status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ferramenta_id")
    private FerramentaEntity ferramenta;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemEntity> imagens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @OneToMany(mappedBy = "anuncio")
    private List<DenunciaEntity> denuncias = new ArrayList<>();

}

