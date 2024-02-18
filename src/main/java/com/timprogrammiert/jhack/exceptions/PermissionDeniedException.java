package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class PermissionDeniedException extends Exception{
    public PermissionDeniedException(String fileName) {
        super(String.format("cannot open '%s': Permission Denied", fileName));
    }
}
