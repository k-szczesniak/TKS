package pl.ks.dk.tks.soapmodel.exceptions;

public class SOAPConverterException extends IllegalArgumentException {

    public SOAPConverterException(String s) {
        super(s);
    }

    public SOAPConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
