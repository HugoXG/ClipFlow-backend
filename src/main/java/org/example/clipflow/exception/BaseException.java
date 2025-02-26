package org.example.clipflow.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{
    private final String msg;

    public BaseException(String message) {
        this.msg = message;
    }
}
