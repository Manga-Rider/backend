package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidFileException extends GlobalServiceException {
    public InvalidFileException(HttpStatusCode status) {
        super(status);
    }

    public InvalidFileException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public InvalidFileException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
