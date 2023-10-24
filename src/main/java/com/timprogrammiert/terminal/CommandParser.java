package com.timprogrammiert.terminal;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.ls.LsCommand;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.user.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tmatz
 */
public class CommandParser {
    private Host host;
    private User currentUser;
    private Map<String, ICommand> commandMap;

    public CommandParser(Host host) {
        commandMap = new HashMap<>();
        this.host = host;
        this.currentUser = this.host.getCurrentUser();
        initCommands();
    }
    private void initCommands(){
        ICommand lsCommand = new LsCommand();
        commandMap.put("ls", lsCommand);
    }

    private String[] substractCommandName(String[] args){
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public void parseCommand(String[] args){
        commandMap.get(args[0]).execute(substractCommandName(args), host);
    }
}
