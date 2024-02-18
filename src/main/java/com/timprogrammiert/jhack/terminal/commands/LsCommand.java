package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.devices.OperatingSystem;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.users.User;
import com.timprogrammiert.jhack.utils.PathResolver;
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
public class LsCommand implements ICommand{
    private static final Logger logger = LoggerFactory.getLogger(LsCommand.class);
    private static final int PATH_INDEX = 0;
    @Override
    public String run(String[] args) {
        try {
            return handleArguments(new ArrayList<>(Arrays.asList(args)));
        } catch (NotADirectoryException | FileNotFoundException | PermissionDeniedException e) {
            return e.getMessage();
        }
    }
    public String handleArguments(List<String> args) throws NotADirectoryException, FileNotFoundException, PermissionDeniedException {
        boolean detailed = args.remove("-al");
        OperatingSystem os = DeviceManager.getCurrentDevice().getOperatingSystem();
        Filesystem filesystem = os.getFilesystem();
        PathResolver pathResolver = new PathResolver(filesystem);
        Directory targetDirectory = args.isEmpty()
                ? filesystem.getCurrentDirectory()
                : getDirectory(pathResolver.resolvePath(args.get(PATH_INDEX)));


        // Throws PermissionDeniedException
        checkPermission(targetDirectory, os.getCurrentUser());

        return detailed ? listDetailed(targetDirectory) : listNormal(targetDirectory).strip();
    }

    private void checkPermission(BaseFile fileToCheck, User user) throws PermissionDeniedException {
        PermissionChecker pemChecker = new PermissionChecker(fileToCheck.getPermission(), user);
        if(!pemChecker.isCanRead()){
            throw new PermissionDeniedException(fileToCheck.getName());
        }
    }

    private Directory getDirectory(BaseFile baseFile) throws NotADirectoryException {
        if(baseFile instanceof Directory){
            return (Directory) baseFile;
        }else {
            throw new NotADirectoryException(baseFile.getName());
        }
    }

    private String listNormal(Directory targetDirectory){
        StringBuilder result = new StringBuilder();
        for (String baseFileName : targetDirectory.getAllFileNames()){
            result.append(baseFileName).append("\n");
        }
        return result.toString().strip();
    }

    private String listDetailed(Directory targetDirectory){
        StringBuilder result = new StringBuilder();
        result.append(getDetailedInfo(targetDirectory, "."));
        result.append(getDetailedInfo(targetDirectory.getParent(), ".."));
        for (BaseFile baseFile : targetDirectory.getChildMap().values()){
            result.append(getDetailedInfo(baseFile, ""));
        }
        return result.toString().strip();
    }

    private String getDetailedInfo(BaseFile baseFile, String aliasName){
        if(baseFile == null){
            logger.debug("BaseFile = null");
            return "";
        }
        String fileName = aliasName.isEmpty() ? baseFile.getName() : aliasName;
        return String.format("%s %s %s %s %s %s %s\n",
                baseFile.getPermission().getPermissionString(),
                baseFile.getPermission().getUser().getUserName(),
                baseFile.getPermission().getGroup().getGroupName(),
                "1",
                "Filesize",
                "modifiedTimestamp",
                fileName);
    }
}
