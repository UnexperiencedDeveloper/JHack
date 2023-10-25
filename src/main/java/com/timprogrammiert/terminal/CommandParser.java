package com.timprogrammiert.terminal;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.ls.LsCommand;
import com.timprogrammiert.filesystem.executable.ExecutableFile;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.user.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code CommandParser} class is responsible for parsing user commands in a simulated Linux environment.
 * It maintains a mapping of command names to their corresponding ICommand implementations and executes
 * the appropriate command based on the user input.
 * <p>
 * Usage:
 * - Create a CommandParser object with the appropriate Host environment: CommandParser parser = new CommandParser(host);
 * - Parse user commands: parser.parseCommand(args);
 *
 * @author tmatz
 * @version 1.0
 */
public class CommandParser {
    private Host host;
    private User currentUser;
    private Map<String, ICommand> commandMap;

    /**
     * Constructs a CommandParser object with the specified host environment.
     *
     * @param host The host environment in which commands are executed.
     */
    public CommandParser(Host host) {
        commandMap = new HashMap<>();
        this.host = host;
        this.currentUser = this.host.getCurrentUser();
        //initCommands();
    }
    private void initCommands(){
        ICommand lsCommand = new LsCommand();
        commandMap.put("ls", lsCommand);
    }

    /**
     * Removes the command name from the array of command arguments.
     *
     * @param args The array of command arguments.
     * @return An array of command arguments without the command name.
     */
    private String[] substractCommandName(String[] args){
        return Arrays.copyOfRange(args, 1, args.length);
    }

    /**
     * Parses and executes the user command based on the provided arguments.
     *
     * @param args The array of command arguments provided by the user.
     */
    public void parseCommand(String[] args){
        // Extract the path from the command arguments (element 0 is the command name, absolute or relative)
        Path path = new Path(args[0]);
        // Resolve the path and execute the corresponding command
        path.resolvePath(host, ExecutableFile.class).getCommand().execute(substractCommandName(args), host);
    }
}
