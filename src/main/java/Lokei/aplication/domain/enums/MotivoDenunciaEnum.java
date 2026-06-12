package Lokei.aplication.domain.enums;

/**
 * Motivos válidos para denúncia de anúncio conforme RN-034.
 */
public enum MotivoDenunciaEnum {
    DISCRIMINACAO("Discriminação"),
    ANUNCIO_FALSO("Anúncio falso"),
    FERRAMENTA_DIFERENTE("Ferramenta diferente da anunciada"),
    DESACORDO_POLITICAS("Desacordo com as políticas");

    private final String descricao;

    MotivoDenunciaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
