package com.mindbowser.exception;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String s) {
        // Call constructor of parent Exception
        super(s);
    }
}

