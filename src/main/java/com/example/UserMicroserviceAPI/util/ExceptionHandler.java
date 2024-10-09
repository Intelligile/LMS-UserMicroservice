
// ExceptionHandler.java
package com.example.UserMicroserviceAPI.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionHandler {


    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Exception ex) {
        return ex.getMessage();
    }
}
