package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NotFoundException extends GlobalServiceException {
    public NotFoundException(HttpStatusCode status) {
        super(status);
    }

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public NotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
