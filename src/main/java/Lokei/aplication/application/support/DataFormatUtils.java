package Lokei.aplication.application.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DataFormatUtils {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DataFormatUtils() {
    }

    public static String formatarData(LocalDate data) {
        return data == null ? null : data.format(DATA);
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora == null ? null : dataHora.format(DATA_HORA);
    }
}
