package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.*;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.utils.CommandRessources;
import com.timprogrammiert.jhack.utils.PathResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class MvCommand implements ICommand{
    private static final int SOURCE_INDEX = 0;
    private static final int DESTINATION_INDEX = 1;
    Computer computer;
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        try {
            return handleArguments(args);
        } catch (NotADirectoryException | FileNotFoundException | InvalidArgumentsException |
                 FileAlreadyExistsException | PermissionDeniedException e) {
            return e.getMessage();
        }
    }

    /**
     * Handles command-line arguments for moving or renaming a file.
     *
     * @param args An array of command-line arguments.
     * @return An empty string if the operation is successful.
     * @throws NotADirectoryException     If the source file is not a directory.
     * @throws FileNotFoundException     If the source file or directory is not found.
     * @throws InvalidArgumentsException  If the number of arguments is less than 2.
     * @throws FileAlreadyExistsException If a file with the same name already exists at the destination.
     */
    private String handleArguments(String[] args) throws NotADirectoryException, FileNotFoundException,
            InvalidArgumentsException, FileAlreadyExistsException, PermissionDeniedException {

        if(args.length < 2){
            throw new InvalidArgumentsException(CommandRessources.MV_USAGE);
        }

        String sourcePath = args[SOURCE_INDEX];
        String destinationPath = args[DESTINATION_INDEX];
        checkMoveOrRenameFile(sourcePath, destinationPath);
        return "";
    }


    /**
     * Checks whether to move or rename a file based on the provided source and destination paths.
     *
     * @param source      The path of the source file or directory.
     * @param destination The path of the destination file or directory.
     * @throws NotADirectoryException     If the source file is not a directory.
     * @throws FileNotFoundException     If the source file or directory is not found.
     * @throws FileAlreadyExistsException If a file with the same name already exists at the destination.
     */
    private void checkMoveOrRenameFile(String source, String destination) throws NotADirectoryException,
            FileNotFoundException, FileAlreadyExistsException, PermissionDeniedException {
        BaseFile sourceFile = getFile(source);
        if(!hasPermissions(sourceFile.getParent())){
            throw new PermissionDeniedException(sourceFile.getParent().getName());
        }

        if (destination.contains("/")) {
            Directory newParent = (Directory) getFile(destination);
            if(!hasPermissions(newParent)){
                throw new PermissionDeniedException(newParent.getName());
            }
            moveFile(sourceFile, newParent, getMoveToDestinationString(sourceFile, destination));
        } else {
            renameFile(sourceFile, destination);
        }
    }


    /**
     * Renames a file or directory to the specified new name.
     *
     * @param file    The file or directory to be renamed.
     * @param newName The new name for the file or directory.
     * @throws FileAlreadyExistsException If a file or directory with the new name already exists in the parent folder.
     */
    private void renameFile(BaseFile file, String newName) throws FileAlreadyExistsException {
        if (file.getParent().hasChild(newName)) {
            throw new FileAlreadyExistsException(newName);
        } else {
            file.setName(newName);
        }
    }


    /**
     * Extracts the destination path for moving a file based on the provided source file and destination path.
     *
     * @param sourceFile   The source file or directory to be moved.
     * @param destination  The destination path where the source file or directory will be moved.
     * @return The extracted destination path for moving the source file.
     */
    private String getMoveToDestinationString(BaseFile sourceFile, String destination) {
        List<String> destinationPathAsList = new ArrayList<>(Arrays.asList(destination.split("/")));

        if (destination.endsWith("/")) {
            // like mv bin var/ -> mv bin /var/bin
            destination = sourceFile.getName();
        } else {
            // like mv bin /var/bin
            destination = destinationPathAsList.getLast();
        }

        destinationPathAsList.remove(destination);

        return String.join("/", destinationPathAsList);
    }


    /**
     * Moves a file or directory to a new parent directory with the specified new name.
     *
     * @param sourceFile   The file or directory to be moved.
     * @param newParent     The new parent directory to move the source file or directory to.
     * @param newFileName   The new name for the file or directory after moving.
     * @throws FileAlreadyExistsException If a file or directory with the new name already exists in the new parent directory.
     */
    private void moveFile(BaseFile sourceFile, Directory newParent, String newFileName) throws FileAlreadyExistsException {
        if (newParent.hasChild(newFileName)) {
            throw new FileAlreadyExistsException(newFileName);
        } else {
            sourceFile.getParent().getChildMap().remove(sourceFile.getName());
            sourceFile.setParentFolder(newParent);
            newParent.addChild(sourceFile);
        }
    }

    private boolean hasPermissions(BaseFile baseFile){
        PermissionChecker pemChecker = new PermissionChecker(baseFile.getPermission(),
                computer.getOperatingSystem().getCurrentUser());
        return pemChecker.isCanWrite();
    }


    /**
     * Retrieves a BaseFile object corresponding to the specified file path.
     *
     * @param filePath The path of the file or directory to be retrieved.
     * @return The BaseFile object representing the file or directory.
     * @throws NotADirectoryException If the specified path is not a directory.
     * @throws FileNotFoundException If the specified file or directory is not found.
     */
    private BaseFile getFile(String filePath) throws NotADirectoryException, FileNotFoundException {
        PathResolver pathResolver = new PathResolver(computer.getOperatingSystem().getFilesystem());
        return pathResolver.resolvePath(filePath);
    }
}
