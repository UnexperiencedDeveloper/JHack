package com.timprogrammiert.commands.ls;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.exceptions.CommandExecutionException;
import com.timprogrammiert.exceptions.PermissionDeniedException;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.host.Host;

import java.util.*;

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
            } else if (argList.get(0).equals(".")) {
                // list current Directory
                listAllChildren(host.getCurrentDirectory());
            } else if (argList.get(0).equals("..")) {
                // list parent Directory
                listAllChildren(host.getCurrentDirectory().getParent());
            } else {
                path = new Path(argList.get(0));
                listAllChildren(path.resolvePath(host, Directory.class));
            }

            }catch (FileObjectNotFoundException | PermissionDeniedException | NullPointerException e){
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
     * Lists all children (files and directories) of the specified {@code baseItem}.
     * If {@code baseItem} is a directory and the user has read permission, it prints
     * the detailed information of the children, including permissions, user names,
     * group names, file sizes, modification timestamps, and names. If detailed listing
     * is not requested, it prints only the names of the children.
     *
     * @param baseItem The {@link FileObject} representing the directory for which
     *                 children are to be listed.
     * @throws PermissionDeniedException If the user does not have permission to read
     *                                   the specified directory.
     */
    private void listAllChildren(FileObject baseItem) throws PermissionDeniedException {
        if (baseItem instanceof Directory directoryObject) {
            StringBuilder stringBuilder = new StringBuilder();
            PermissionChecker pemChecker = new PermissionChecker(baseItem, host);
            if (!pemChecker.isCanRead()) {
                throw new PermissionDeniedException(String.format("cannot open directory '%s': permission denied", baseItem.getName()));
            }

            if (detailedList) {
                printDetails(directoryObject);
                detailedList = false;
            } else {
                Collection<FileObject> children = directoryObject.getAllChildren();
                for (FileObject object : children) {
                    // Append only the names of files/directories
                    stringBuilder.append(object.getName()).append("\n");
                }
            }

            // Print the list of children (detailed or simple) to the console if its not empty
            if (!stringBuilder.toString().isEmpty()) {
                System.out.println(stringBuilder.toString().strip());
            }
        }

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

    private void printDetails(Directory directory) throws NullPointerException{
        TableFormatter tableFormatter = new TableFormatter(directory);
        TablePrinter tablePrinter = new TablePrinter(tableFormatter);
        tablePrinter.printTable();
    }
}
