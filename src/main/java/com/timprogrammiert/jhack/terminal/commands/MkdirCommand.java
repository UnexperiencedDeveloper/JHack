package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.utils.DirectoryCreator;
import com.timprogrammiert.jhack.utils.PathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class MkdirCommand implements ICommand{
    private static final Logger logger = LoggerFactory.getLogger(MkdirCommand.class);
    Computer computer;

    @Override
    public String run(String[] args) {
        List<String> argList = Arrays.asList(args);
        computer = DeviceManager.getCurrentDevice();
        try {
            handleArguments(argList);
        } catch (InvalidArgumentsException | NotADirectoryException  |
                 PermissionDeniedException | FileAlreadyExistsException e) {
            return e.getMessage();
        }
        return "";
    }

    private void handleArguments(List<String> argList) throws InvalidArgumentsException, NotADirectoryException, FileAlreadyExistsException, PermissionDeniedException {
        if(argList.isEmpty()){
            throw new InvalidArgumentsException("Usage MKDIR STILL MISSING");
        }
        String fileName = argList.getFirst();
        PathResolver pathResolver = new PathResolver(computer.getOperatingSystem().getFilesystem());
        try {
            // Check File exists
            BaseFile checkExistingFile =  pathResolver.resolvePath(fileName);
            throw new FileAlreadyExistsException(checkExistingFile.getName());
        } catch (FileNotFoundException e) {
            // Create Directory
            if(fileName.startsWith("/")){
                // Global Path
                Directory rootFolder = computer.getOperatingSystem().getFilesystem().getRootFolder();
                DirectoryCreator.createDirectory(fileName, rootFolder, computer);
            }else {
                // local path
                Directory currentDirectory = computer.getOperatingSystem().getFilesystem().getCurrentDirectory();
                DirectoryCreator.createDirectory(fileName, currentDirectory, computer);
            }
        }
        logger.debug(String.format("Created directory: %s", fileName));
    }

}
