package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class GroupDoesNotExistException extends Exception{
    public GroupDoesNotExistException(String groupName) {
        super(String.format("Group %s does not exist", groupName));
    }
}
