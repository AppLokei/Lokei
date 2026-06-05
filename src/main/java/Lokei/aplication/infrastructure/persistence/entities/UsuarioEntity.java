package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String telefone;

    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<AnuncioEntity> anuncios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<DenunciaEntity> denuncias = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;
}
