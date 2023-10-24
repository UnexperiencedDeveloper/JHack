package com.timprogrammiert.terminal;

import com.timprogrammiert.host.Host;
import com.timprogrammiert.user.User;

import java.util.Map;

/**
 * @author tmatz
 */
public class CommandParser {
    private Host host;
    private User currentUser;

    public CommandParser(Host host) {
        this.host = host;
        this.currentUser = this.host.getCurrentUser();
    }

    public void parseCommand(String[] args){

    }
}
