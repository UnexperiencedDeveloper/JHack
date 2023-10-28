package com.timprogrammiert.commands.cd;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.commands.exceptions.PermissionDeniedException;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tmatz
 * @version 1.0
 */
public class CdCommand implements ICommand {
    private Host host;
    private String commandName;
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        this.host = host;
        commandName = "cd";
        Directory targetDirectory;
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        try {
            if (argList.isEmpty()) {
                System.out.println("empty arglist"); // do nothing
            } else if (argList.get(0).equals("..")) {
                if(host.getCurrentDirectory().getParent() != null){
                    changeDirectory(host.getCurrentDirectory().getParent());
                }
            } else {
                Path path = new Path(argList.get(0));
                targetDirectory = path.resolvePath(host, Directory.class);
                changeDirectory(targetDirectory);
            }
        } catch (FileObjectNotFoundException | PermissionDeniedException e) {
            throw new CommandExecutionException(commandName + ": " + e.getMessage());
        }

    }

    private void changeDirectory(Directory directoryToCd) throws PermissionDeniedException {
        PermissionChecker pemChecker = new PermissionChecker(directoryToCd, host);
        // Directories needs Execute Permission to Cd in
        // Linux errors silently when Cd in a directory without permission, we dont want do it silently
        if (pemChecker.isCanExecute()) {
            host.setCurrentDirectory(directoryToCd);
            directoryToCd.getFileMetaData().setAccessedTimeStamp();
        } else {
            throw new PermissionDeniedException(String.format("cannot open directory '%s': permission denied", directoryToCd.getName()));
        }

    }
}
