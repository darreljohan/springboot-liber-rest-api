package com.iglo.exam.liber.error.exception;

public class DeletionConflict extends  RuntimeException{
    public DeletionConflict(String message) {
        super(message);
    }
}
