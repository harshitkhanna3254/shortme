package com.comp519.shortme.exceptions;

public class UrlNotFoundException extends RuntimeException{

    public UrlNotFoundException(String message){
        super(message);
    }
}
