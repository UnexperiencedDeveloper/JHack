package com.timprogrammiert.terminal;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
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
    }

    /**
     * Parses the given command arguments and executes the corresponding command.
     *
     * @param args The array of command arguments, where the first element is the command name (absolute or relative path).
     */
    public void parseCommand(String[] args){
        //TODO EXCEPTION HANDLING

        // Extract the command name from the arguments (element 0 is the command name, absolute or relative)
        String commandName = args[0];
        if(commandName.isEmpty()) {
            // Do nothing
            return;
        }

        // Attempt to resolve the command using the given command name
        ICommand commandToExecute = resolveCommand(commandName);

        try {
            // If the command is not found in the given path, Iterate through environment paths and attempt command resolution
            if (commandToExecute == null) {
                for (String envPath : Path.EnvironmentVariable.split(":")) {
                    commandToExecute = resolveCommand(envPath + "/" + commandName);
                    // If command is found, execute it and break the loop
                    if (commandToExecute != null) {
                        commandToExecute.execute(substractCommandName(args), host);
                        break;
                    } else {
                        System.out.println("Command not found - There should be an Exception. Called From Command Parser");
                        return;
                    }
                }
            } else {
                // Command was found in given Path
                commandToExecute.execute(substractCommandName(args), host);
            }
        } catch (CommandExecutionException e) {
            System.out.print(e.getMessage());
        }

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
     * Resolves the given command path to an ICommand object.
     *
     * @param commandPath The path to the executable command.
     * @return An ICommand object representing the resolved command, or null if not found.
     */
    private ICommand resolveCommand(String commandPath){
        Path path = new Path(commandPath);
        ExecutableFile executableFile = null;
        try {
            executableFile = path.resolvePath(host, ExecutableFile.class);
        }catch (FileObjectNotFoundException e){
            //TODO ADD EXCEPTION HANDLE
        }

        return (executableFile != null) ? executableFile.getCommand() : null;
    }
}
