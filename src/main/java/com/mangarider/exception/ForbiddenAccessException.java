package com.mangarider.exception;

import org.springframework.http.HttpStatusCode;

public class ForbiddenAccessException extends GlobalServiceException{
    public ForbiddenAccessException(HttpStatusCode status) {
        super(status);
    }

    public ForbiddenAccessException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
