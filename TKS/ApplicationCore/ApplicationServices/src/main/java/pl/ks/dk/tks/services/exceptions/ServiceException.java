package pl.ks.dk.tks.services.exceptions;

public class ServiceException extends IllegalArgumentException{
    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
