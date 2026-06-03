package co.edu.uptc.project2.util;

/**
 * Clase utilitaria con validaciones mediante expresiones regulares.
 * Centraliza todas las reglas de validación del sistema.
 */
public class Validator {

    // ── Expresiones regulares ────────────────────────────────────────────────

    /** Solo letras (mayúsculas, minúsculas, tildes, ñ) y espacios. Mín. 2 caracteres. */
    private static final String REGEX_NAME = "^[a-zA-ZÁÉÍÓÚáéíóúÑñÜü ]{2,}$";

    /** Formato estándar de correo electrónico. */
    private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** Número de teléfono colombiano: 10 dígitos, empieza por 3. */
    private static final String REGEX_PHONE = "^3[0-9]{9}$";

    /** Número de documento: solo dígitos, entre 6 y 12 caracteres. */
    private static final String REGEX_DOCUMENT_NUMBER = "^[0-9]{6,12}$";

    /** Opción de menú: un solo dígito del 0 al 9. */
    private static final String REGEX_MENU_OPTION = "^[0-9]$";

    /** Número entero positivo (cantidad de productos, etc.). */
    private static final String REGEX_POSITIVE_INTEGER = "^[1-9][0-9]*$";

    /** Número decimal positivo (precios, duraciones). Acepta punto como separador. */
    private static final String REGEX_POSITIVE_DECIMAL = "^[0-9]+(\\.[0-9]+)?$";

    /** Código de factura: letras, números y guión. Ej: FAC-001 */
    private static final String REGEX_INVOICE_CODE = "^[A-Za-z0-9\\-]{3,20}$";

    /** Fecha en formato YYYY-MM-DD. */
    private static final String REGEX_DATE = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    // ── Métodos de validación ────────────────────────────────────────────────

    public static boolean isValidName(String value) {
        return value != null && value.matches(REGEX_NAME);
    }

    public static boolean isValidEmail(String value) {
        return value != null && value.matches(REGEX_EMAIL);
    }

    public static boolean isValidPhone(String value) {
        return value != null && value.matches(REGEX_PHONE);
    }

    public static boolean isValidDocumentNumber(String value) {
        return value != null && value.matches(REGEX_DOCUMENT_NUMBER);
    }

    public static boolean isValidMenuOption(String value) {
        return value != null && value.matches(REGEX_MENU_OPTION);
    }

    public static boolean isValidPositiveInteger(String value) {
        return value != null && value.matches(REGEX_POSITIVE_INTEGER);
    }

    public static boolean isValidPositiveDecimal(String value) {
        return value != null && value.matches(REGEX_POSITIVE_DECIMAL);
    }

    public static boolean isValidInvoiceCode(String value) {
        return value != null && value.matches(REGEX_INVOICE_CODE);
    }

    public static boolean isValidDate(String value) {
        return value != null && value.matches(REGEX_DATE);
    }

    // ── Mensajes de error estándar ───────────────────────────────────────────

    public static final String MSG_NAME =
            "  ✗ Solo letras, tildes y espacios. Mínimo 2 caracteres.";

    public static final String MSG_EMAIL =
            "  ✗ Formato inválido. Ejemplo: usuario@dominio.com";

    public static final String MSG_PHONE =
            "  ✗ Debe tener 10 dígitos y empezar por 3. Ejemplo: 3001234567";

    public static final String MSG_DOCUMENT_NUMBER =
            "  ✗ Solo números, entre 6 y 12 dígitos.";

    public static final String MSG_MENU_OPTION =
            "  ✗ Ingrese un número de opción válido.";

    public static final String MSG_POSITIVE_INTEGER =
            "  ✗ Debe ser un número entero mayor a 0.";

    public static final String MSG_POSITIVE_DECIMAL =
            "  ✗ Debe ser un número positivo (ej: 1500 o 1500.50).";

    public static final String MSG_INVOICE_CODE =
            "  ✗ Código inválido. Use letras, números y guiones. Ej: FAC-001";

    public static final String MSG_DATE =
            "  ✗ Formato inválido. Use YYYY-MM-DD. Ej: 2026-05-27";
}
