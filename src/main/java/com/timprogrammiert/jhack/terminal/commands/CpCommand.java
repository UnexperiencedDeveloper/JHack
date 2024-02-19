package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.filesystem.File;
import com.timprogrammiert.jhack.permissions.Permission;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.permissions.PermissionUtil;
import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;
import com.timprogrammiert.jhack.utils.CommandRessources;
import com.timprogrammiert.jhack.utils.PathResolver;

import java.security.Permissions;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class CpCommand implements ICommand{
    private static final int SOURCE_PATH_INDEX = 0;
    private static final int TARGET_PATH_INDEX = 1;
    Computer computer;
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        return handleArguments(args);
    }

    /**
     * Handles the command-line arguments for the 'cp' (copy) operation.
     * Copies a source file or directory to a target directory.
     *
     * @param args Command-line arguments.
     * @return Error message if any exception occurs, otherwise an empty string.
     */
    private String handleArguments(String[] args) {

        // Resolve paths using the PathResolver
        PathResolver pathResolver = new PathResolver(computer.getOperatingSystem().getFilesystem());

        try {
            if(args.length < 2){
                throw new InvalidArgumentsException(CommandRessources.CP_USAGE);
            }

            // Resolve source and target paths to obtain BaseFile objects
            BaseFile sourceFile = pathResolver.resolvePath(args[SOURCE_PATH_INDEX]);
            BaseFile targetFolder = pathResolver.resolvePath(args[TARGET_PATH_INDEX]);

            // Check if the target is a directory and initiate the copy process
            if (targetFolder instanceof Directory targetDirectory) {
                processCopy(sourceFile, targetDirectory);
            } else {
                throw new NotADirectoryException(targetFolder.getName());
            }
        } catch (FileNotFoundException | NotADirectoryException | PermissionDeniedException |
                 InvalidArgumentsException e) {
            // Handle exceptions and return an error message
            return e.getMessage();
        }

        // Return an empty string if the operation is successful
        return "";
    }

    /**
     * Processes the copy operation for a source file or directory to a target directory.
     *
     * @param sourceFile     The source file or directory to copy.
     * @param targetFolder   The target directory to copy to.
     * @throws PermissionDeniedException If permissions are insufficient for the operation.
     */
    private void processCopy(BaseFile sourceFile, Directory targetFolder) throws PermissionDeniedException {
        // Check if the user can write into the new folder
        if (!hasFolderPermissions(targetFolder)) {
            throw new PermissionDeniedException(targetFolder.getName());
        }

        // Check if the source file can be read
        if (!hasFilePermissions(sourceFile)) {
            throw new PermissionDeniedException(sourceFile.getName());
        }

        if (sourceFile instanceof Directory sourceDirectory) {
            // Handle Directory Copy
            copyDirectory(sourceDirectory, targetFolder);
        } else {
            // Handle File Copy
            copyFile(sourceFile, targetFolder);
        }
    }

    /**
     * Recursively copies a source directory to a target directory.
     *
     * @param source          The source directory to copy.
     * @param targetDirectory The target directory to copy to.
     */
    private void copyDirectory(Directory source, Directory targetDirectory) {
        // Check if the target directory already contains the source directory
        // If it contains a directory with the same name, use it; otherwise, create a new directory with a different name
        if (!targetDirectory.getChildMap().containsKey(source.getName())) {
            targetDirectory = createDirectory(source.getPermission().getPermissionString(), source.getName(), targetDirectory);
        } else {
            // Check if there's a already a File with the name of the Directory
            BaseFile targetObject = targetDirectory.getChildByName(source.getName());
            if (targetObject instanceof Directory dir) {
                targetDirectory = dir;
            } else {
                targetDirectory = createDirectory(source.getPermission().getPermissionString(), source.getName() + "_DIR", targetDirectory);
            }
        }

        // Iterate through source directory content and copy each item
        for (BaseFile file : source.getChildMap().values()) {
            if (file instanceof Directory dir) {
                // Recursively copy subdirectories
                if (hasFolderPermissions(dir)) {
                    copyDirectory(dir, targetDirectory);
                }
            } else {
                // Copy files
                copyFile(file, targetDirectory);
            }
        }
    }

    /**
     * Copies a source file to a target directory.
     *
     * @param sourceFile      The source file to copy.
     * @param targetDirectory The target directory to copy to.
     */
    private void copyFile(BaseFile sourceFile, Directory targetDirectory) {
        // Create a new file with a modified name if the file already exists
        createNewFile(sourceFile.getPermission().getPermissionString(), sourceFile.getName(), targetDirectory);
    }

    /**
     * Creates a new file in the specified directory.
     *
     * @param permissionString The permission string for the new file.
     * @param fileName         The name of the new file.
     * @param parentDirectory  The parent directory for the new file.
     */
    private void createNewFile(String permissionString, String fileName, Directory parentDirectory) {
        User currentUser = computer.getOperatingSystem().getCurrentUser();
        Group currentGroup = currentUser.getPrimaryGroup();

        Permission permissions = PermissionUtil.createDefaultFilePermission(currentUser, currentGroup);

        // If the file with the same name already exists, create a new file with a modified name
        if (parentDirectory.getChildMap().containsKey(fileName)) {
            createNewFile(permissionString, fileName + "_COPY", parentDirectory);
        }

        // Create a new file
        new File(fileName, parentDirectory, permissions);
    }

    /**
     * Creates a new directory with the specified permissions, name, and parent directory.
     *
     * @param permissionString The permission string for the new directory.
     * @param dirName          The name of the new directory.
     * @param parentDirectory  The parent directory for the new directory.
     * @return The newly created directory.
     */
    private Directory createDirectory(String permissionString, String dirName, Directory parentDirectory) {
        // Get the current user and group
        User currentUser = computer.getOperatingSystem().getCurrentUser();
        Group currentGroup = currentUser.getSpecificGroup(currentUser.getUserName());

        // Create permissions for the new directory
        Permission permissions = PermissionUtil.createDefaultDirectoryPermission(currentUser, currentGroup);

        // Create and return a new directory object
        return new Directory(dirName, parentDirectory, permissions);
    }

    /**
     * Checks if the user has execute and write permissions on the specified directory.
     *
     * @param dir The directory to check for permissions.
     * @return True if the user has execute and write permissions, false otherwise.
     */
    private boolean hasFolderPermissions(Directory dir) {
        // Use PermissionChecker to check execute and write permissions
        PermissionChecker pemChecker = new PermissionChecker(dir.getPermission(), computer.getOperatingSystem().getCurrentUser());
        return pemChecker.isCanExecute() && pemChecker.isCanWrite();
    }

    /**
     * Checks if the user has read permissions for the specified file.
     *
     * @param file The file to check for read permissions.
     * @return True if the user has read permissions, false otherwise.
     */
    private boolean hasFilePermissions(BaseFile file) {
        // Use PermissionChecker to check read permissions
        PermissionChecker pemChecker = new PermissionChecker(file.getPermission(), computer.getOperatingSystem().getCurrentUser());
        return pemChecker.isCanRead();
    }

}
