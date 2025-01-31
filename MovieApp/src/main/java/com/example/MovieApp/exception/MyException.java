package com.example.MovieApp.exception;

import org.springframework.validation.BindingResult;

public class MyException extends RuntimeException {

    private BindingResult br;

    // Constructor that accepts a String message
    public MyException(String message) {
        super(message); // Call the superclass constructor with the message
    }

    // Constructor that accepts both a String message and BindingResult
    public MyException(String message, BindingResult br) {
        super(message);
        this.br = br;
    }

    public BindingResult getBr() {
        return br;
    }

    public void setBr(BindingResult br) {
        this.br = br;
    }
}
