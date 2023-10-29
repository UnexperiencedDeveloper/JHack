package com.timprogrammiert.exceptions;

/**
 * @author tmatz
 * @version 1.0
 */
public class PermissionDeniedException extends Exception{
    public PermissionDeniedException(String message) {
        super(message);
    }
}
