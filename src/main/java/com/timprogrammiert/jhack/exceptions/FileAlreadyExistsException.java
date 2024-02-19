package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class FileAlreadyExistsException extends Exception{
    public FileAlreadyExistsException(String fileName) {
        super(String.format("'%s' already exists", fileName));
    }
}
