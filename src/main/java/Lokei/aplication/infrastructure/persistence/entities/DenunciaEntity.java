package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "tb_denuncia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DenunciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String motivo;
    private String descricao;

    @CreationTimestamp
    @Column(name = "data_denuncia")
    private LocalDate dataDenuncia;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private AnuncioEntity anuncio;
}
