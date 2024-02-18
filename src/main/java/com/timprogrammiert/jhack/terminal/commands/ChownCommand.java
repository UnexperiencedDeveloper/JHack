package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.*;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;
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
public class ChownCommand implements ICommand{
    private static Logger logger = LoggerFactory.getLogger(ChownCommand.class);
    // Constants for command flags and argument indices
    private final static String RECURSIVE_FLAG = "-R";
    private final static int FILE_PATH_INDEX = 1;
    Computer computer;
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        return handleArguments(args);
    }

    /**
     * Handles the provided arguments for the chown command.
     *
     * @param args The arguments provided for the chown command.
     * @return A message or an empty string indicating the result of the command.
     */
    private String handleArguments(String[] args) {
        try {
            // Check for the minimum required number of arguments
            if (args.length < 2) {
                throw new InvalidArgumentsException("Usage: chown user:group /path/to/directory");
            }

            // Convert array to list for easy manipulation
            List<String> argList = new ArrayList<>(Arrays.asList(args));
            boolean recursive = argList.remove(RECURSIVE_FLAG);
            String userName = "";
            String groupName = "";


            if(argList.getFirst().startsWith(":")){
                // :group
                String[] userAndGroup = argList.getFirst().split(":");
                userName = computer.getOperatingSystem().getCurrentUser().getUserName();
                groupName = userAndGroup[1];
            } else if (argList.getFirst().endsWith(":")) {
                // user:
                String[] userAndGroup = argList.getFirst().split(":");
                userName = userAndGroup[0];
            }else if (argList.getFirst().contains(":")) {
                // user:group
                String[] userAndGroup = argList.getFirst().split(":");
                userName = userAndGroup[0];
                groupName = userAndGroup[1];
            } else {
                userName = argList.getFirst();
            }


            // Resolve the path to the target file/directory
            Filesystem filesystem = computer.getOperatingSystem().getFilesystem();
            BaseFile targetFile = new PathResolver(filesystem).resolvePath(argList.get(FILE_PATH_INDEX));

            // Process ownership changes based on provided arguments
            processFileOwnership(userName, groupName, targetFile, recursive);
        } catch (FileNotFoundException | NotADirectoryException | InvalidArgumentsException |
                 PermissionDeniedException | UserDoesNotExistException | GroupDoesNotExistException e) {
            // Return error message if an exception occurs during processing
            return e.getMessage();
        }

        // Return an empty string indicating successful execution
        return "";
    }

    /**
     * Processes ownership changes based on user and group information.
     *
     * @param userName   The name of the user to set ownership.
     * @param groupName  The name of the group to set ownership.
     * @param targetFile The target file/directory for ownership changes.
     * @param recursive  A flag indicating whether ownership changes should be recursive.
     * @throws InvalidArgumentsException If the provided arguments are invalid.
     */
    private void processFileOwnership(String userName, String groupName, BaseFile targetFile, boolean recursive)
            throws InvalidArgumentsException, PermissionDeniedException, UserDoesNotExistException, GroupDoesNotExistException {
        PermissionChecker pemChecker = new PermissionChecker(targetFile.getPermission(),
                computer.getOperatingSystem().getCurrentUser());

        // Check Permissions
        // TODO - SUDO USER'S MISSING
        // Regular Users can only Change Groups
        if(!pemChecker.isCanWrite()){throw new PermissionDeniedException(targetFile.getName());}

        if (groupName.isEmpty() && isValidUser(userName)) {
            // Change User Only
            User userToSet = computer.getOperatingSystem().getSpecificUser(userName);
            Group currentSetGroup = targetFile.getPermission().getGroup();
            updateOwnership(targetFile, userToSet, currentSetGroup, recursive);

        } else if (!groupName.isEmpty() && isValidUser(userName) && isValidGroup(groupName)) {
            // Change User and Group
            User userToSet = computer.getOperatingSystem().getSpecificUser(userName);
            Group groupToSet = computer.getOperatingSystem().getSpecificGroup(groupName);
            updateOwnership(targetFile, userToSet, groupToSet, recursive);

        } else if (userName.isEmpty() && isValidGroup(groupName)) {
            // change Group only
            User userToSet = targetFile.getPermission().getUser();
            Group currentSetGroup = computer.getOperatingSystem().getSpecificGroup(groupName);
            updateOwnership(targetFile, userToSet, currentSetGroup, recursive);

        } else {
            // Throw an exception if arguments are invalid
            throw new InvalidArgumentsException("Usage: chown user:group /path/to/file");
        }
    }
    /**
     * Updates ownership of the target file/directory.
     *
     * @param targetFile The target file/directory for ownership changes.
     * @param user       The user to set ownership.
     * @param group      The group to set ownership.
     * @param recursive  A flag indicating whether ownership changes should be recursive.
     */
    private void updateOwnership(BaseFile targetFile, User user, Group group, boolean recursive) {
        // Set ownership for individual files
        targetFile.getPermission().setUser(user);
        targetFile.getPermission().setGroup(group);
        logger.debug(String.format("Changed ownership for %s to user: %s group: %s",
                targetFile.getName(),
                user.getUserName(),
                group.getGroupName()));
        // If the target is a directory and recursive flag is set, update ownership recursively
        if (targetFile instanceof Directory directoryObject && recursive) {
            for (BaseFile file : RecursiveFinder.getAllRecursive(directoryObject)) {
                file.getPermission().setUser(user);
                file.getPermission().setGroup(group);
                logger.debug(String.format("Changed ownership for %s to user: %s group: %s",
                        file.getName(),
                        user.getUserName(),
                        group.getGroupName()));
            }
        }
    }
    /**
     * Checks if the User is Valid (exists)
     * @param userName The User to check
     * @return True if User is valid - False if not
     */
    private boolean isValidUser(String userName) throws UserDoesNotExistException {
        if(!computer.getOperatingSystem().isValidUser(userName))
        {
            throw new UserDoesNotExistException(userName);
        }
        return true;
    }

    /**
     * Checks if the Group is Valid (exists)
     * @param groupName The group to check
     * @return True if User is valid - False if not
     */
    private boolean isValidGroup(String groupName) throws GroupDoesNotExistException {
        if(!computer.getOperatingSystem().isValidGroup(groupName)){
            throw new GroupDoesNotExistException(groupName);
        }
        return true;
    }
}
