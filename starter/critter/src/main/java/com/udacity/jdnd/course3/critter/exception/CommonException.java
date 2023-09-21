package com.udacity.jdnd.course3.critter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)//
public class CommonException extends RuntimeException{

    public CommonException() {
        super("Can't found");
    }

    public CommonException(String message) {
        super(message);
    }
}