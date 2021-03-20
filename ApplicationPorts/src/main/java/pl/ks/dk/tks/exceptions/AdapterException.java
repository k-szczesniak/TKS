package pl.ks.dk.tks.exceptions;

public class AdapterException extends IllegalArgumentException {
    public AdapterException(String s) {
        super(s);
    }

    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
