package ar.com.enrique.apimanager.exception;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -6187344735723810275L;

    public BadRequestException(String message) {
        super(message);
    }

}
