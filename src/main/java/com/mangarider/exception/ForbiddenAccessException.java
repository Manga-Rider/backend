package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ForbiddenAccessException extends GlobalServiceException{
    public ForbiddenAccessException(HttpStatusCode status) {
        super(status);
    }

    public ForbiddenAccessException(String data) {
        super(HttpStatus.FORBIDDEN, data);
    }

    public ForbiddenAccessException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
