package Lokei.aplication.domain.enums;

public enum statusAluguelEnum {
    EM_APROVACAO(1),
    CONFIRMADO(2),
    ATIVO(3),
    CONCLUIDO(4),
    CANCELADO(5),
    REPROVADO(6);

    private int code;

    private statusAluguelEnum(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static statusAluguelEnum valueOf(int code) {
        for (statusAluguelEnum value : statusAluguelEnum.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("invalid OrderStatus code");
    }
}
