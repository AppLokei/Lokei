package Lokei.aplication.application.util;

import java.util.regex.Pattern;

public class CpfOrEmailUtil {

    private static final Pattern CPF_PATTERN =
            Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$|^\\d{11}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public enum IdentifierType {
        CPF,
        EMAIL
    }

    /**
     * Identifies whether the given string is a CPF or an e-mail address.
     *
     * @param value the string to evaluate
     * @return {@link IdentifierType#CPF} if the value matches a CPF pattern,
     *         {@link IdentifierType#EMAIL} if it matches an e-mail pattern
     * @throws IllegalArgumentException if the value does not match either pattern
     */
    public static IdentifierType identify(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O valor fornecido não pode ser nulo ou vazio.");
        }

        String trimmed = value.trim();

        if (CPF_PATTERN.matcher(trimmed).matches()) {
            return IdentifierType.CPF;
        }

        if (EMAIL_PATTERN.matcher(trimmed).matches()) {
            return IdentifierType.EMAIL;
        }

        throw new IllegalArgumentException(
                "O valor \"" + trimmed + "\" não é um CPF nem um e-mail válido.");
    }
}
