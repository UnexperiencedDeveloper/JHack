package com.timprogrammiert.commands.ls;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.commands.exceptions.PermissionDeniedException;
import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The {@code LsCommand} class implements the ICommand interface to execute the 'ls' command in a simulated
 * Linux file system environment. It can list the contents of the current directory or a specified directory.
 * Supports the '-al' option for detailed listing.
 * <p>
 * Usage:
 * - To list contents of the current directory: LsCommand.execute(new String[]{}, host);
 * - To list contents of a specific directory: LsCommand.execute(new String[]{path}, host);
 *
 * @author tmatz
 * @version 1.0
 * @see ICommand
 */
public class LsCommand implements ICommand {
    private Host host; // The host environment in which the command is executed
    private boolean detailedList = false; // Flag indicating whether detailed listing is requested
    private Path path; // The path of the directory to list (optional) only set if not the current directory to list
    private final String commandName = "ls";

    /**
     * Executes the 'ls' command based on the provided arguments and the current host environment.
     *
     * @param args The command arguments, where the first argument (if provided) is the path to the directory.
     * @param host The Host object representing the system state.
     * @throws CommandExecutionException If there is an error executing the command.
     */
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        this.host = host;
        List<String> argList = parseArgumentsForTags(new ArrayList<>(Arrays.asList(args)));
        try {
            if(argList.isEmpty()){
                // If no specific path provided, list files and directories in the current directory
                listCurrentDirectory();
            }else {
                path = new Path(argList.get(0));
                listAllChildren(path.resolvePath(host, Directory.class));
            }

            }catch (PermissionDeniedException e){
                System.out.println(e.getMessage());
            }catch (FileObjectNotFoundException e){
                throw new CommandExecutionException(commandName + ": " + e.getMessage());
            }
    }

    /**
     * Lists the contents of the current directory in the host environment.
     */
    private void listCurrentDirectory() throws PermissionDeniedException {
        listAllChildren(host.getCurrentDirectory());
    }

    /**
     * Lists all children (files and directories) of the specified base directory.
     *
     * @param baseItem The base directory whose contents need to be listed.
     */
    private void listAllChildren(FileObject baseItem) throws PermissionDeniedException {
        StringBuilder stringBuilder = new StringBuilder();
        if(baseItem instanceof Directory directoryObject){
            Collection<FileObject> children = directoryObject.getAllChildren();
            for (FileObject object: children) {
                PermissionChecker pemChecker = new PermissionChecker(object, host.getCurrentUser());
                if(!pemChecker.isCanRead()){
                    throw new PermissionDeniedException(String.format("%s: cannot open directory '/%s': permission denied", commandName, object.getName()));
                }
                if(detailedList){
                    // Append detailed file information: permissions, user, group, size, modification timestamp, and name
                    FileMetaData metaData = object.getFileMetaData();
                    stringBuilder.append(metaData.getFilePermission().getPermissionString()).append(" ")
                            .append(metaData.getFilePermission().getUser().getUserName()).append(" ")
                            .append(metaData.getFilePermission().getUserGroup().getGroupName()).append(" ")
                            .append("FileSize").append(" ")
                            .append(metaData.getModifiedTimeStamp()).append(" ")
                            .append(object.getName()).append("\n");
                }else {
                    // Append only the names of files/directories
                    stringBuilder.append(object.getName()).append("\n");
                }
            }
            // Print the list of children (detailed or simple) to the console if its not empty
            if(!stringBuilder.toString().isEmpty()){
                System.out.println(stringBuilder.toString().strip());
            }

        }
        detailedList = false;
    }

    /**
     * Parses the command arguments and extracts options/tags.
     *
     * @param argList A list of command arguments to be parsed.
     * @return A list of parsed arguments without the options/tags.
     */
    private List<String> parseArgumentsForTags(List<String> argList){
        if(argList.contains("-al")){
            detailedList = true;
            argList.remove("-al");
        }
        return argList;
    }
}
