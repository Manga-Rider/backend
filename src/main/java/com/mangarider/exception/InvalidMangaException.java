package com.mangarider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidMangaException extends GlobalServiceException {
    public InvalidMangaException(HttpStatusCode status) {
        super(status);
    }

    public InvalidMangaException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public InvalidMangaException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
