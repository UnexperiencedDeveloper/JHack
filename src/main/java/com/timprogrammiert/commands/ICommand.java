package com.timprogrammiert.commands;

import com.timprogrammiert.exceptions.CommandExecutionException;
import com.timprogrammiert.host.Host;

/**
 * @author tmatz
 */
public interface ICommand {
    void execute(String[] args, Host host) throws CommandExecutionException;
}
