package pl.ks.dk.tks.soapmodel.exceptions;

public class UserSoapConverterException extends IllegalArgumentException {

    public UserSoapConverterException(String s) {
        super(s);
    }

    public UserSoapConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
