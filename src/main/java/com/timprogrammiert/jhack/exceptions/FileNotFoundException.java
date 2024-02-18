package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class FileNotFoundException extends Exception{
    public FileNotFoundException(String fileName) {
        super(String.format("cannot access '%s': No such file or directory", fileName));
    }
}
