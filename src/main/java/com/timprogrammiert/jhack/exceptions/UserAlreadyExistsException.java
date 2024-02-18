package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String userName) {
        super(String.format("Cannot create User: %s: User already exists", userName));
    }
}
