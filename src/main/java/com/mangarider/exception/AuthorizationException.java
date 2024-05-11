package com.mangarider.exception;

import org.springframework.http.HttpStatusCode;

public class AuthorizationException extends GlobalServiceException {
    public AuthorizationException(HttpStatusCode status) {
        super(status);
    }

    public AuthorizationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
