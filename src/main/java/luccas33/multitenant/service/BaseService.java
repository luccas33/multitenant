package luccas33.multitenant.service;

import luccas33.multitenant.application.StatusException;
import org.springframework.http.HttpStatus;

public class BaseService {

    StatusException logAndThrow(String message, HttpStatus status, Throwable t) {
        message = message == null ? "" : message;
        t = t == null ? new RuntimeException(message) : t;
        status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
        if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            t.printStackTrace();
        }
        return new StatusException(message, t, status);
    }

}
