package com.projects.petshopNew.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataBaseException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public DataBaseException(String msg){
        super(msg);
    }
}
