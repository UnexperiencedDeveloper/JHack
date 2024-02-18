package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class GroupAlreadyExistsException extends Exception{
    public GroupAlreadyExistsException(String groupName) {
        super(String.format("Cannot create Group: %s: Group already exists", groupName));
    }
}
