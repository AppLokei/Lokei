package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_denuncia")
public class DenunciaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String motivo;
    private String descricao;
    private Date dataDenuncia;

    public DenunciaEntity(){

    }

    public DenunciaEntity(Integer id, String motivo, String descricao, Date dataDenuncia) {
        this.id = id;
        this.motivo = motivo;
        this.descricao = descricao;
        this.dataDenuncia = dataDenuncia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(Date dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DenunciaEntity denunciaEntity = (DenunciaEntity) o;
        return Objects.equals(id, denunciaEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
