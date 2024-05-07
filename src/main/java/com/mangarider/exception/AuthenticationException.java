package com.mangarider.exception;

import org.springframework.http.HttpStatusCode;

public class AuthenticationException extends GlobalServiceException {
    public AuthenticationException(HttpStatusCode status) {
        super(status);
    }

    public AuthenticationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
