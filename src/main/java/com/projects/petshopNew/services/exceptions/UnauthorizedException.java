package com.projects.petshopNew.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    private String customMessage;

    public UnauthorizedException(String msg){
        super(msg);
        this.customMessage = msg;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
