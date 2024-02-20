package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.utils.CommandRessources;
import com.timprogrammiert.jhack.utils.PathResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Tim
 * Date: 20.02.2024
 * Version: 1.0
 */
public class RmCommand implements ICommand{
    Computer computer;
    private static final String FORCE_REMOVE_TAG = "-rf";
    private static final int FILE_PATH_INDEX = 0;
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        try {
            return handleCommand(new ArrayList<>(Arrays.asList(args)));
        }catch (InvalidArgumentsException | FileNotFoundException | NotADirectoryException e){
            return e.getMessage();
        }

    }

    private String handleCommand(List<String> args) throws NotADirectoryException, FileNotFoundException, InvalidArgumentsException {
        boolean forceRemove = args.remove(FORCE_REMOVE_TAG);

        if (args.isEmpty()) {
            throw new InvalidArgumentsException(CommandRessources.RM_USAGE);
        }

        String filePath = args.get(FILE_PATH_INDEX);
        PathResolver pathResolver = new PathResolver(computer.getOperatingSystem().getFilesystem());
        BaseFile baseFile = pathResolver.resolvePath(filePath);

        deleteFileOrDirectory(baseFile, forceRemove);

        return "";
    }

    private void deleteFileOrDirectory(BaseFile baseFile, boolean forceRemove)
            throws NotADirectoryException, InvalidArgumentsException {
        if (baseFile instanceof Directory directory) {
            if (directory.getChildMap().isEmpty() || forceRemove) {
                directory.getParent().removeChild(directory.getName());
            } else {
                throw new InvalidArgumentsException(directory.getName() + " is not empty!");
            }
        } else {
            baseFile.getParent().removeChild(baseFile.getName());
        }
    }

}
