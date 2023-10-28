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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final Host host;
    private static final Logger logger = Logger.getLogger(CommandParser.class.getName());

    /**
     * Constructs a CommandParser object with the specified host environment.
     *
     * @param host The host environment in which commands are executed.
     */
    public CommandParser(Host host) {
        this.host = host;
    }

    /**
     * Parses the given command arguments and executes the corresponding command.
     *
     * @param args The array of command arguments, where the first element is the command name (absolute or relative path).
     */
    public void parseCommand(String[] args){

        // Extract the command name from the arguments (element 0 is the command name, absolute or relative)
        String commandName = args[0];
        if(commandName.isEmpty()) {
            // Do nothing
            return;
        }

        ICommand commandToExecute = null;
        boolean commandFound = false;

        try {
            // Attempt to resolve the command using the given command name
            commandToExecute = resolveCommand(commandName);
            if (commandToExecute != null) {
                commandFound = true;
            }
        } catch (FileObjectNotFoundException searchInEnvPath) {
            // Command not found in the initial path, attempt to find it in other paths
            for (String envPath : Path.EnvironmentVariable.split(":")) {
                try {
                    commandToExecute = resolveCommand(envPath + "/" + commandName);
                    if (commandToExecute != null) {
                        commandFound = true;
                        break;
                    }
                } catch (FileObjectNotFoundException ignored) {
                    // Ignored because looping over EnrionmentVariable -> Ment to fail sometimes
                }
            }
        }

        if (commandFound) {
            executeCommand(commandToExecute, args);
        } else {
            logger.log(Level.WARNING, "Command: " + commandName + " not found");
        }
    }

    private void executeCommand(ICommand commandToExecute, String[] args){
        try {
            commandToExecute.execute(substractCommandName(args), host);
        } catch (CommandExecutionException exception) {
            logger.log(Level.WARNING, exception.getMessage());
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
    private ICommand resolveCommand(String commandPath) throws FileObjectNotFoundException {
        Path path = new Path(commandPath);
        ExecutableFile executableFile = null;
        executableFile = path.resolvePath(host, ExecutableFile.class);

        return (executableFile != null) ? executableFile.getCommand() : null;
    }
}
