package com.timprogrammiert.jhack.terminal;

import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.CommandDoesNotExistException;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.filesystem.PathUtils;
import com.timprogrammiert.jhack.gui.components.TerminalTextArea;
import com.timprogrammiert.jhack.terminal.commands.ICommand;
import com.timprogrammiert.jhack.terminal.commands.LsCommand;
import com.timprogrammiert.jhack.terminal.commands.TestList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    TerminalTextArea terminalTextArea;
    String terminalPrefix;
    Filesystem filesystem;
    public CommandHandler(TerminalTextArea terminalTextArea) {
        this.terminalTextArea = terminalTextArea;
        filesystem = DeviceManager.getCurrentDevice().getOperatingSystem().getFilesystem();
        terminalPrefix = PathUtils.pathToString(filesystem.getCurrentDirectory());
        terminalTextArea.appendText(String.format("%s ", terminalPrefix));
    }

    public void handleInput(String[] input){
        if(input.length > 0){
            try {
                String commandResult = getCommand(input).run(input);
                if(!commandResult.isEmpty()){
                    terminalTextArea.appendText("\n" + commandResult);
                }
                //terminalTextArea.appendText("\n" + getCommand(input).run(input));
            }catch (Exception e){
                terminalTextArea.appendText("\n" + e.getMessage());
                //throw new RuntimeException(e.getMessage());
            }
        }
        logger.debug(Arrays.toString(input));

        putTerminalPrefix();
    }
    private void putTerminalPrefix(){
        terminalTextArea.appendText(String.format("\n%s ", terminalPrefix));
    }

    private ICommand getCommand(String[] input) throws CommandDoesNotExistException {
        return switch (input[0]){
            case "ls" -> new LsCommand();
            default -> throw new CommandDoesNotExistException(input[0]);
        };
    }

}
