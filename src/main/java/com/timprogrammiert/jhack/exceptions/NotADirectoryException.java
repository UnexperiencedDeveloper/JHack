package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class NotADirectoryException extends Exception{
    public NotADirectoryException(String fileName) {
        super(String.format("%s: Not a directory"));
    }
}
