package edu.odu.cs.gold.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice(basePackages = {"edu.odu.cs.gold.controller"} )
public class ParkODUControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String error() {
        return "/error/error.html";
    }

}