package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException(String userName) {
        super(String.format("User: %s does not exist", userName));
    }
}
