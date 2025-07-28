package com.dhanashri.Quiz_Service.Exception;

public class ResourceNotFoundException  extends RuntimeException{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
