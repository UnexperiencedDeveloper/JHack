package com.timprogrammiert.commands.chmod;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.exceptions.CommandExecutionException;
import com.timprogrammiert.exceptions.PermissionDeniedException;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.FilePermission;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.filesystem.permission.PermissionUtil;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The `ChmodCommand` class represents a command in a file system application that allows users
 * to change permissions of a specified file or directory. It implements the `Command` interface
 * and provides functionality for executing the 'chmod' command with given arguments on a host system.
 * The class contains methods for parsing command arguments, checking permissions, updating file
 * permissions, and modifying timestamps of the target file and its parent directory.
 * @author tmatz
 * @version 1.0
 */
public class ChmodCommand implements ICommand {
    private Host host;
    private final String commandName = "chmod";

    /**
     * Executes the 'chmod' command with the given arguments and host context.
     *
     * @param args The command arguments, where args[0] is the permission mode and args[1] is the file path.
     * @param host The host context representing the current system environment.
     * @throws CommandExecutionException If there is an error during command execution.
     */
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        this.host = host;
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        changeFilePermissions(argList);
    }

    /**
     * Changes the permissions of the specified file or directory based on the given command arguments.
     *
     * @param argList The list of command arguments, where argList.get(0) is the permission mode and argList.get(1) is the file path.
     * @throws CommandExecutionException If there is an error during permission change operation.
     */
    private void changeFilePermissions(List<String> argList) throws CommandExecutionException{
        if(argList.isEmpty()) return; // Do nothing
        try {
            String permissionNumber = parsePermissionNumber(argList);
            Path path = parsePath(argList);
            FileObject targetFile = resolveTargetFile(path);
            checkPermission(targetFile);
            updateFilePermissions(targetFile, permissionNumber);
            updateTimeStamps(targetFile);
        } catch (PermissionDeniedException | FileObjectNotFoundException | NumberFormatException e) {
            throw new CommandExecutionException(commandName + ": " + e);
        }
    }

    /**
     * Checks if permission change is allowed for the specified file or directory.
     *
     * @param targetFile The target file or directory.
     * @throws PermissionDeniedException If permission change is not permitted.
     */
    private void checkPermission(FileObject targetFile) throws PermissionDeniedException{
        PermissionChecker permissionChecker = new PermissionChecker(targetFile, host);
        PermissionChecker permissionCheckerParent = new PermissionChecker(targetFile.getParent(), host);

        if (!permissionChecker.isCanWrite() || !permissionCheckerParent.isCanWrite()) {
            throw new PermissionDeniedException(String.format("changing permissions of '%s' : Operation not permitted", targetFile.getName()));
        }
    }

    /**
     * Updates the permissions of the specified file or directory.
     *
     * @param targetFile      The target file or directory.
     * @param permissionNumber The new permission number as a string.
     */
    private void updateFilePermissions(FileObject targetFile, String permissionNumber){
        FilePermission originalFilePermission = targetFile.getFileMetaData().getFilePermission();
        FilePermission updatedFilePermission = PermissionUtil.changePermission(originalFilePermission, permissionNumber);
        targetFile.getFileMetaData().setFilePermission(updatedFilePermission);
    }

    /**
     * Updates the modified timestamps of the specified file and its parent directory.
     *
     * @param targetFile The target file or directory.
     */
    private void updateTimeStamps(FileObject targetFile){
        targetFile.getFileMetaData().setModifiedTimeStamp();
        targetFile.getParent().getFileMetaData().setModifiedTimeStamp();
    }

    /**
     * Parses the permission number from the command arguments and validates its format.
     *
     * @param argList The list of command arguments.
     * @return The parsed permission number as a string.
     * @throws NumberFormatException    If the permission number is invalid or completely missing.
     * @throws PermissionDeniedException If permission change is not permitted because the String is 000.
     */
    private String parsePermissionNumber(List<String> argList) throws NumberFormatException, PermissionDeniedException{
        String pemString = argList.get(0);
        if (isInteger(pemString)) {
            validateInputFormat(pemString);
            validatePermissionRange(pemString);
            return pemString;
        } else {
            // If the input is not a valid integer, it's invalid
            throw new NumberFormatException("missing operand");
        }
    }

    /**
     * Validates the input format of the permission number.
     *
     * @param pemString The permission number to be validated.
     * @throws NumberFormatException If the input does not match the required format.
     *                               Throws an exception with a detailed message indicating the position
     *                               of the invalid character in the input string.
     */
    private void validateInputFormat(String pemString)throws NumberFormatException{
        if (!pemString.matches("\\d{3}")) {
            throw new NumberFormatException(String.format("missing operand after '%s'", pemString.charAt(pemString.length() - 1)));
        }
    }

    /**
     * Validates the permission range of the input permission number.
     *
     * @param pemString The permission number to be validated.
     * @throws PermissionDeniedException If the input is not in the valid range [000-777] or is "000".
     *                                   Throws an exception indicating that the operation is not permitted.
     */
    private void validatePermissionRange(String pemString) throws PermissionDeniedException {
        if (!pemString.matches("[0-7]{3}")) {
            throw new PermissionDeniedException("Operation not permitted");
        }

        if (pemString.equals("000")) {
            throw new PermissionDeniedException("Operation not permitted");
        }
    }

    /**
     * Checks if the given input is a valid integer.
     *
     * @param input The input string.
     * @return True if the input is a valid integer, false otherwise.
     */
    private boolean isInteger(String input){
        return input.matches("\\d+");
    }

    /**
     * Parses the file path from the command arguments.
     *
     * @param argList The list of command arguments.
     * @return The parsed file path as a Path object.
     */
    private Path parsePath(List<String> argList){
        return new Path(argList.get(1));
    }

    /**
     * Resolves the target file or directory based on the given path.
     *
     * @param path The file path.
     * @return The resolved FileObject representing the target file or directory.
     * @throws FileObjectNotFoundException If the target file or directory is not found.
     */
    private FileObject resolveTargetFile(Path path) throws FileObjectNotFoundException{
        return path.resolvePath(host, FileObject.class);
    }
}
