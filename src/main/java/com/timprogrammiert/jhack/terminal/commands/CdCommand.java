package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.devices.OperatingSystem;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.utils.PathResolver;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class CdCommand implements ICommand {
    private static final int PATH_INDEX = 0;
    OperatingSystem os;
    Filesystem filesystem;
    @Override
    public String run(String[] args) {
        os =  DeviceManager.getCurrentDevice().getOperatingSystem();
        filesystem = os.getFilesystem();

        if(args.length < 1){
            try {
                throw new InvalidArgumentsException("Should Change to User HomeDir");
            } catch (InvalidArgumentsException e) {
                return e.getMessage();
            }
        }
        return changeDirectory(args[PATH_INDEX]);
    }

    private String changeDirectory(String pathStringToCd){
        PathResolver pathResolver = new PathResolver(filesystem);
        Directory targetDirectory;

        try {
            BaseFile resolvedFile = pathResolver.resolvePath(pathStringToCd);
            if(resolvedFile instanceof Directory){
                targetDirectory = (Directory) resolvedFile;
                PermissionChecker pemChecker = new PermissionChecker(targetDirectory.getPermission(), os.getCurrentUser());
                if(!(pemChecker.isCanExecute() && pemChecker.isCanRead())) throw new PermissionDeniedException(targetDirectory.getName());
                filesystem.setCurrentDirectory(targetDirectory);
            }else {
                throw new NotADirectoryException(resolvedFile.getName());
            }
        } catch (NotADirectoryException | FileNotFoundException | PermissionDeniedException e) {
            return e.getMessage();
        }

        return "";
    }
}
