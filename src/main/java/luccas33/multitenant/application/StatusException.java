package luccas33.multitenant.application;

import org.springframework.http.HttpStatus;

public class StatusException extends Exception {

    private final HttpStatus status;

    public StatusException(HttpStatus status) {
        this.status = status;
    }

    public StatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public StatusException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public StatusException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public StatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
