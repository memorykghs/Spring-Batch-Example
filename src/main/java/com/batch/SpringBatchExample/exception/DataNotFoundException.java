package com.batch.SpringBatchExample.exception;

public class DataNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public DataNotFoundException(String msg) {
        super(msg);
    }

}
