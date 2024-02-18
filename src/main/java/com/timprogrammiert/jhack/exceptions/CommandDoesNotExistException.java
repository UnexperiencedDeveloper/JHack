package com.timprogrammiert.jhack.exceptions;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class CommandDoesNotExistException extends Exception{
    public CommandDoesNotExistException(String commandName) {
        super(String.format("Command not found: %s: command not found", commandName));
    }
}
