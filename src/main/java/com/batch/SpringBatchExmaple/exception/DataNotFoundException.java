package com.batch.SpringBatchExmaple.exception;

public class DataNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public DataNotFoundException() {}
    
    public DataNotFoundException(String msg) {
        super(msg);
    }

}
