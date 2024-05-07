package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class UserAlreadyExistsException extends GlobalServiceException{
    public UserAlreadyExistsException(HttpStatusCode status) {
        super(status);
    }

    public UserAlreadyExistsException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }

    public UserAlreadyExistsException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
