package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.permissions.PermissionUtil;
import com.timprogrammiert.jhack.utils.PathResolver;
import com.timprogrammiert.jhack.utils.RecursiveFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class ChmodCommand implements ICommand{
    private static final Logger logger = LoggerFactory.getLogger(ChmodCommand.class);
    Computer computer;
    private final static int FILE_PATH_INDEX = 1;
    private final static String RECURSIVE_TAG = "-R";
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        try {
            return handleArguments(new ArrayList<>(Arrays.asList(args)));
        } catch (NotADirectoryException | PermissionDeniedException | InvalidArgumentsException |
                 FileNotFoundException e) {
            return e.getMessage();
        }
    }

    /**
     * Handles the provided arguments for the Chmod command.
     *
     * @param argList The list of command-line arguments.
     * @return A String representing the result of the Chmod operation.
     * @throws NotADirectoryException     If the resolved path does not represent a directory.
     * @throws FileNotFoundException      If the specified path is not found.
     * @throws InvalidArgumentsException   If the provided arguments are invalid.
     * @throws PermissionDeniedException   If the user lacks the necessary permissions.
     */
    private String handleArguments(List<String> argList) throws NotADirectoryException, FileNotFoundException,
            InvalidArgumentsException, PermissionDeniedException {
        if(argList.size() <= 1){
            throw new InvalidArgumentsException("Usage: chown 770 file");
        }
        boolean recursive = argList.remove(RECURSIVE_TAG);
        String permissionCode = argList.getFirst();

        if(!(isValid(permissionCode))){throw new InvalidArgumentsException("Invalid permission Code");}
        String filePath = argList.get(FILE_PATH_INDEX);
        PathResolver pathResolver = new PathResolver(computer.getOperatingSystem().getFilesystem());
        BaseFile file = pathResolver.resolvePath(filePath);

        if(recursive && file instanceof Directory directoryObject){
            changeRecursive(directoryObject, permissionCode);
        }else {
            changeLocal(file, permissionCode);
        }
        return "";
    }

    /**
     * Recursively changes the permissions of the specified directory and its subdirectories.
     *
     * @param targetFile      The target directory to start the recursive permission change.
     * @param permissionCode  The permission code to be applied to the files and directories.
     * @throws InvalidArgumentsException  Thrown if the permission code is invalid.
     * @throws PermissionDeniedException  Thrown if the permission change is denied.
     */
    private void changeRecursive(Directory targetFile, String permissionCode)
            throws InvalidArgumentsException, PermissionDeniedException {
        // Iterate through all files in the directory and its subdirectories
        for (BaseFile file : RecursiveFinder.getAllRecursive(targetFile)) {
            // Delegate the permission change to a common method
            handleFilePermission(file, permissionCode);
        }
    }

    /**
     * Changes the permissions of the specified file or directory locally, without recursion.
     *
     * @param file            The file or directory for which permissions should be changed.
     * @param permissionCode  The permission code to be applied to the file or directory.
     * @throws InvalidArgumentsException  Thrown if the permission code is invalid.
     * @throws PermissionDeniedException  Thrown if the permission change is denied.
     */
    private void changeLocal(BaseFile file, String permissionCode)
            throws InvalidArgumentsException, PermissionDeniedException {
        // Delegate the permission change to a common method
        handleFilePermission(file, permissionCode);
    }

    /**
     * Handles the permission change for the specified file or directory.
     *
     * @param file            The file or directory for which permissions should be changed.
     * @param permissionCode  The permission code to be applied to the file or directory.
     * @throws InvalidArgumentsException  Thrown if the permission code is invalid.
     * @throws PermissionDeniedException  Thrown if the permission change is denied.
     */
    private void handleFilePermission(BaseFile file, String permissionCode)
            throws InvalidArgumentsException, PermissionDeniedException {

        // Check and update permissions based on file type and current permissions
        if (hasPermissionToChange(file)) {
            logger.debug(String.format("Changed permissions for %s to %s",file.getName(), permissionCode));
            String newPemCode =  PermissionUtil.permissionCodeToString(permissionCode, file);
            file.getPermission().setPermissionString(newPemCode);
        } else {
            // Throw exception if permission change is denied
            throw new PermissionDeniedException(file.getName());
        }
    }

    private boolean hasPermissionToChange(BaseFile file){
        PermissionChecker pemChecker = new PermissionChecker(file.getPermission(), computer.getOperatingSystem().getCurrentUser());
        if(file instanceof Directory){
            return (pemChecker.isCanWrite() && pemChecker.isCanExecute())  ;
        }
        return pemChecker.isCanWrite();
    }

    /**
     * Checks if the given permission arguments are valid.
     *
     * @param permissionArguments The permission arguments to validate.
     * @return True if the input type is correct; otherwise, false.
     */
    private boolean isValid(String permissionArguments) {
        // Checks only if the Input type is Correct, not the actual numeric values
        if(permissionArguments.equals("+x")){
            return true;
        } else return (permissionArguments.length() == 3 && isNumeric(permissionArguments));
    }

    /**
     * Checks if a given string represents a numeric value.
     *
     * @param str The string to check.
     * @return True if the string is numeric; otherwise, false.
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
