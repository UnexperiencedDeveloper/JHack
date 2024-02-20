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
import com.timprogrammiert.jhack.users.User;
import com.timprogrammiert.jhack.utils.CommandRessources;
import com.timprogrammiert.jhack.utils.FormatTableOuput;
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
            // Delegate to the handleArguments function for processing
            return handleArguments(new ArrayList<>(Arrays.asList(args)));
        } catch (NotADirectoryException | FileNotFoundException | PermissionDeniedException | InvalidArgumentsException e) {
            // Handle exceptions and return error messages
            return e.getMessage();
        }
    }

    /**
     * Handles the provided arguments and performs the necessary operations.
     *
     * @param args The list of command arguments.
     * @return The result of the command execution or an error message.
     * @throws NotADirectoryException    If the target is not a directory.
     * @throws FileNotFoundException     If the file is not found.
     * @throws PermissionDeniedException If there is a permission issue.
     */
    public String handleArguments(List<String> args) throws NotADirectoryException, FileNotFoundException, PermissionDeniedException, InvalidArgumentsException {
        if(args.contains("-h")){
            throw new InvalidArgumentsException(CommandRessources.LS_USAGE);
        }
        // Check if the '-al' flag is present in the arguments
        boolean detailed = args.remove("-al");

        // Retrieve the operating system, filesystem, and path resolver
        OperatingSystem os = DeviceManager.getCurrentDevice().getOperatingSystem();
        Filesystem filesystem = os.getFilesystem();
        PathResolver pathResolver = new PathResolver(filesystem);

        // Determine the target directory based on the arguments
        Directory targetDirectory = args.isEmpty()
                ? filesystem.getCurrentDirectory()
                : getDirectory(pathResolver.resolvePath(args.get(PATH_INDEX)));

        // Check permissions for the target directory
        checkPermission(targetDirectory, os.getCurrentUser());

        // Return either detailed or normal listing based on the flag
        return detailed ? listDetailed(targetDirectory) : listNormal(targetDirectory).strip();
    }

    /**
     * Checks if the current user has read permission for the given file.
     *
     * @param fileToCheck The file for which permissions are checked.
     * @param user        The current user.
     * @throws PermissionDeniedException If there is a permission issue.
     */
    private void checkPermission(BaseFile fileToCheck, User user) throws PermissionDeniedException {
        PermissionChecker pemChecker = new PermissionChecker(fileToCheck.getPermission(), user);
        if (!pemChecker.isCanRead()) {
            throw new PermissionDeniedException(fileToCheck.getName());
        }
    }

    /**
     * Retrieves a Directory from a BaseFile, throwing an exception if it is not a Directory.
     *
     * @param baseFile The file to convert to a Directory.
     * @return The Directory corresponding to the BaseFile.
     * @throws NotADirectoryException If the file is not a Directory.
     */
    private Directory getDirectory(BaseFile baseFile) throws NotADirectoryException {
        if (baseFile instanceof Directory) {
            return (Directory) baseFile;
        } else {
            throw new NotADirectoryException(baseFile.getName());
        }
    }

    /**
     * Creates a normal listing of files in the target directory.
     *
     * @param targetDirectory The directory for which to create a listing.
     * @return The normal listing as a string.
     */
    private String listNormal(Directory targetDirectory) {
        StringBuilder result = new StringBuilder();
        for (String baseFileName : targetDirectory.getAllFileNames()) {
            result.append(baseFileName).append("\n");
        }
        return result.toString().strip();
    }

    /**
     * Creates a detailed listing of files in the target directory.
     *
     * @param targetDirectory The directory for which to create a detailed listing.
     * @return The detailed listing as a string.
     */
    private String listDetailed(Directory targetDirectory) {
        StringBuilder result = new StringBuilder();
        result.append(getDetailedInfo(targetDirectory, "."));
        result.append(getDetailedInfo(targetDirectory.getParent(), ".."));
        for (BaseFile baseFile : targetDirectory.getChildMap().values()) {
            result.append(getDetailedInfo(baseFile, ""));
        }
        return FormatTableOuput.getTableOutput(result.toString()).strip();
    }

    /**
     * Creates detailed information about a file for a listing.
     *
     * @param baseFile   The file for which to create detailed information.
     * @param aliasName  An optional alias name to display in the listing.
     * @return Detailed information about the file as a string.
     */
    private String getDetailedInfo(BaseFile baseFile, String aliasName) {
        if (baseFile == null) {
            // Log a debug message and return an empty string for null BaseFile
            logger.debug("BaseFile = null");
            return "";
        }

        // Determine the name to display in the listing
        String fileName = aliasName.isEmpty() ? baseFile.getName() : aliasName;

        // Format and return detailed information about the file
        return String.format("%s %s %s %s %s %s %s\n",
                baseFile.getPermission().getPermissionString(),
                baseFile.getPermission().getUser().getUserName(),
                baseFile.getPermission().getGroup().getGroupName(),
                "1", // Placeholder for file count (e.g., for directories)
                baseFile.getMetaData().getFileSizeObject().getFileSize(), // Placeholder for file size information
                baseFile.getMetaData().getModifiedDate(), // Placeholder for modification timestamp
                fileName);
    }

}
