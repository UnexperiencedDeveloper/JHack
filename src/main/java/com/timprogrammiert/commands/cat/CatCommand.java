package com.timprogrammiert.commands.cat;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.exceptions.CommandExecutionException;
import com.timprogrammiert.exceptions.PermissionDeniedException;
import com.timprogrammiert.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.filesystem.regularFile.RegularFile;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tmatz
 * @version 1.0
 */
public class CatCommand implements ICommand {
    private Host host;
    private Path path;
    private final String commandName = "cat";
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        try {
            List<String> argList = new ArrayList<>(Arrays.asList(args));
            if(argList.isEmpty()) {
                // Do nothing
            }
            else  {
                path = new Path(argList.get(0));
                RegularFile fileToCat = path.resolvePath(host, RegularFile.class);
                PermissionChecker permissionChecker = new PermissionChecker(fileToCat, host);
                if (permissionChecker.isCanRead()) {
                    printContent(path.resolvePath(host, RegularFile.class));
                } else {
                    throw new PermissionDeniedException(String.format("%s: cannot open file '/%s': permission denied", commandName, fileToCat.getName()));
                }
            }
        } catch (FileObjectNotFoundException | PermissionDeniedException e) {
            throw new CommandExecutionException(commandName + ": " + e.getMessage());
        }
    }

    private void printContent(RegularFile regularFile){
        System.out.println(regularFile.getContent());
    }
}
