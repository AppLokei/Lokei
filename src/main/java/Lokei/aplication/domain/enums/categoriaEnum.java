package Lokei.aplication.domain.enums;

public enum categoriaEnum {
    Alicates(1),
    Aparadores_e_Cortadores_de_Grama(2),
    Betoneiras(3),
    Caixas_e_Maletas_de_Ferramentas(4),
    Chaves_de_Fenda(5),
    Equipamentos_de_proteção_Individual(6),
    Escadas(7),
    Esmilhadeiras(8),
    Esquadros_FitasMetricas_e_Trenas(9),
    Furadeiras_e_Parafusadeiras(10),
    Lixadeiras(11),
    Martelos(12),
    Serras_e_Motoserras(13),
    Outros(13);

    private int code;

    private categoriaEnum(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static categoriaEnum valueOf(int code) {
        for (categoriaEnum value : categoriaEnum.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("invalid OrderStatus code");
    }
}
