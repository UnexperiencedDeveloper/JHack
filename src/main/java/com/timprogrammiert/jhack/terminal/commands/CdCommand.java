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
import com.timprogrammiert.jhack.utils.CommandRessources;
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
    /**
     * Executes the 'cd' (change directory) command.
     *
     * @param args The array of command arguments.
     * @return A message indicating the result of the 'cd' command.
     */
    @Override
    public String run(String[] args) {
        // Retrieve the current operating system and filesystem
        os = DeviceManager.getCurrentDevice().getOperatingSystem();
        filesystem = os.getFilesystem();

        // Check if there are enough arguments
        if (args.length < 1) {
            try {
                // Throw an exception for invalid arguments
                throw new InvalidArgumentsException(CommandRessources.CD_USAGE);
            } catch (InvalidArgumentsException e) {
                // Return the exception message
                return e.getMessage();
            }
        }

        // Call the changeDirectory method with the specified path
        return changeDirectory(args[PATH_INDEX]);
    }

    /**
     * Changes the current working directory to the specified path.
     *
     * @param pathStringToCd The path to change to.
     * @return A message indicating the result of the directory change.
     */
    private String changeDirectory(String pathStringToCd) {
        // Initialize necessary components
        PathResolver pathResolver = new PathResolver(filesystem);
        Directory targetDirectory;

        try {
            // Resolve the path to a BaseFile
            BaseFile resolvedFile = pathResolver.resolvePath(pathStringToCd);

            // Check if the resolved file is a Directory
            if (resolvedFile instanceof Directory) {
                // Cast the resolved file to a Directory
                targetDirectory = (Directory) resolvedFile;

                // Check permissions before changing directory
                PermissionChecker pemChecker = new PermissionChecker(targetDirectory.getPermission(), os.getCurrentUser());

                // Check if the user has execute and read permissions
                if (!(pemChecker.isCanExecute() && pemChecker.isCanRead())) {
                    throw new PermissionDeniedException(targetDirectory.getName());
                }

                // Change the current directory in the filesystem
                filesystem.setCurrentDirectory(targetDirectory);
            } else {
                // Throw an exception if the resolved file is not a Directory
                throw new NotADirectoryException(resolvedFile.getName());
            }
        } catch (NotADirectoryException | FileNotFoundException | PermissionDeniedException e) {
            // Return the exception message if an exception occurs
            return e.getMessage();
        }

        // Return an empty string indicating success
        return "";
    }

}
