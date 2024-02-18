package com.timprogrammiert.jhack.terminal;

import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.CommandDoesNotExistException;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.filesystem.PathUtils;
import com.timprogrammiert.jhack.gui.components.TerminalTextArea;
import com.timprogrammiert.jhack.terminal.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final TerminalTextArea terminalTextArea;
    private String terminalPrefix;
    private final Filesystem filesystem;

    /**
     * Constructs a CommandHandler with a reference to the TerminalTextArea.
     *
     * @param terminalTextArea The TerminalTextArea to interact with.
     */
    public CommandHandler(TerminalTextArea terminalTextArea) {
        this.terminalTextArea = terminalTextArea;

        // Initialize the filesystem based on the current device's operating system
        filesystem = DeviceManager.getCurrentDevice().getOperatingSystem().getFilesystem();

        // Set the initial terminal prefix to the current working directory
        terminalPrefix = PathUtils.pathToString(filesystem.getCurrentDirectory());

        // Display the initial terminal prompt
        terminalTextArea.appendText(String.format("%s ", terminalPrefix));
    }

    /**
     * Handles the input commands provided in the terminal.
     *
     * @param input The array of input commands.
     */
    public void handleInput(String[] input) {
        if (input.length > 0) {
            try {
                // Execute the command and get the result
                String commandResult = getCommand(input).run(removeCommandName(input));

                // Display the command result in the terminal
                if (!commandResult.isEmpty()) {
                    terminalTextArea.appendText("\n" + commandResult);
                }
            } catch (Exception e) {
                // Display any exception messages in the terminal
                terminalTextArea.appendText("\n" + e.getMessage());
            }
        }

        // Log the input for debugging purposes
        logger.debug(Arrays.toString(input));

        // Display the terminal prefix for the next command
        putTerminalPrefix();
    }

    /**
     * Displays the current working directory as the terminal prefix.
     */
    private void putTerminalPrefix() {
        terminalPrefix = PathUtils.pathToString(filesystem.getCurrentDirectory());
        terminalTextArea.appendText(String.format("\n%s ", terminalPrefix));
    }

    /**
     * Gets the appropriate command based on the input command name.
     *
     * @param input The array of input commands.
     * @return An implementation of the ICommand interface corresponding to the input command name.
     * @throws CommandDoesNotExistException If the input command does not exist.
     */
    private ICommand getCommand(String[] input) throws CommandDoesNotExistException {
        return switch (input[0]) {
            case "ls" -> new LsCommand();
            case "cd" -> new CdCommand();
            case "id" -> new IdCommand();
            case "chown" -> new ChownCommand();
            default -> throw new CommandDoesNotExistException(input[0]);
        };
    }

    /**
     * Removes the command name from the input array.
     *
     * @param input The array of input commands.
     * @return A new array without the first element (command name).
     */
    private String[] removeCommandName(String[] input) {
        // Convert the array to a list, remove the first element, and convert back to an array
        List<String> convertedList = new ArrayList<>(Arrays.asList(input));
        convertedList.removeFirst();
        return convertedList.toArray(new String[0]);
    }

}
