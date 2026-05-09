package Lokei.aplication.infrastructure.persistence.enums;

public enum statusAnuncioEnum {
    ATIVO(1),
    PAUSADO(2),
    DESATIVADO(3);

    private int code;

    private statusAnuncioEnum(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static statusAnuncioEnum valueOf(int code) {
        for (statusAnuncioEnum value : statusAnuncioEnum.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("invalid OrderStatus code");
    }
}
