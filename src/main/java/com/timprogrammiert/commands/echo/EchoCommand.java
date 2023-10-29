package com.timprogrammiert.commands.echo;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.exceptions.CommandExecutionException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tmatz
 * @version 1.0
 */
public class EchoCommand implements ICommand {
    private Host host;
    private Path path;
    private final String commandName = "echo";
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        if(argList.isEmpty()) return; // Do nothing
        else if (argList.get(0).startsWith("\"")) {
            String output = String.join(" ", argList);
            System.out.println(output.substring(1, output.length() - 1));
        }
    }


}
