package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ConflictException extends GlobalServiceException {
    public ConflictException(HttpStatusCode status) {
        super(status);
    }

    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }

    public ConflictException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
