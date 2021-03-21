package pl.ks.dk.tks.exceptions;

public class DTOConverterException extends IllegalArgumentException {
    public DTOConverterException(String s) {
        super(s);
    }

    public DTOConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
