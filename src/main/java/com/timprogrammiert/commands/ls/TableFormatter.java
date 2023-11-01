package com.timprogrammiert.commands.ls;

import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The `TableFormatter` class is responsible for formatting the tabular representation
 * of a collection of `FileObject` items. It calculates the maximum width for each
 * column (user name, group name, file size) and formats the details of each `FileObject`
 * into a table with columns for permissions, user name, group name, file size, modification
 * timestamp, and name.
 * <p>
 * The class utilizes a `DateTimeFormatter` for formatting modification timestamps and
 * a `StringBuilder` to construct the formatted table. It handles cases where the `currentDirectory`
 * and `parentDirectory` need to be represented as "." and ".." respectively in the table.
 * <p>
 * The formatted table can be obtained by calling the `getFormattedTable()` method.
 *
 * @author tmatz
 * @version 1.0
 */
public class TableFormatter {
    private int maxUserNameLength;
    private int maxSizeLength;
    private int maxGroupNameLength;
    private final List<FileObject> fileObjectToList;
    private final Directory currentDirectory, parentDirectory;

    /**
     * Constructor for the `TableFormatter` class. Initializes the class with a
     * `Directory` object to be formatted into a table.
     *
     * @param directoryToList The `Directory` object whose contents will be formatted.
     */
    public TableFormatter(Directory directoryToList){
        currentDirectory = directoryToList;
        parentDirectory = directoryToList.getParent();

        fileObjectToList = new ArrayList<>();
        fileObjectToList.add(currentDirectory);

        // Include parent directory if it exists
        if (parentDirectory != null) {
            fileObjectToList.add(parentDirectory);
        }

        // Add all children of the given directory to the list
        fileObjectToList.addAll(directoryToList.getAllChildren());
    }
    /**
     * Calculates the maximum width for each column in the table by iterating
     * through the collection of `FileObject` items. It calculates the maximum
     * lengths for user names, group names, and file sizes.
     */
    private void calculateWidth() {
        // Initialize maximum column widths
        maxUserNameLength = 0;
        maxSizeLength = 0;
        maxGroupNameLength = 0;

        // Iterate through the list of file objects and calculate maximum column widths
        for (FileObject object : fileObjectToList) {
            FileMetaData metaData = object.getFileMetaData();
            maxGroupNameLength = Math.max(maxGroupNameLength, metaData.getFilePermission().getUserGroup().getGroupName().length());
            maxUserNameLength = Math.max(maxUserNameLength, metaData.getFilePermission().getUser().getUserName().length());
            maxSizeLength = Math.max(maxSizeLength, String.valueOf(metaData.getFileSize().getFileSize()).length());
        }
    }

    /**
     * Formats the `FileObject` items into a table and returns the formatted table as a string.
     * The table includes columns for permissions, user name, group name, file size, modification
     * timestamp, and name. The current directory is represented as ".", the parent directory as "..",
     * and regular files/directories by their names.
     *
     * @return The formatted table as a string.
     * @throws NullPointerException If there are null values encountered during formatting.
     */
    public String getFormattedTable() throws NullPointerException{
        // Date and time formatter for modification timestamps
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd HH:mm", Locale.ENGLISH);

        // StringBuilder to hold the formatted table
        StringBuilder stringBuilder = new StringBuilder();

        // Calculate column widths
        calculateWidth();

        // Iterate through the list of file objects and format them into a table
        for (FileObject object : fileObjectToList) {
            FileMetaData metaData = object.getFileMetaData();
            String permission = metaData.getFilePermission().getPermissionString();
            String userName = metaData.getFilePermission().getUser().getUserName();
            String groupName = metaData.getFilePermission().getUserGroup().getGroupName();
            String fileSize = String.valueOf(metaData.getFileSize().getFileSize());
            String timeStamp = metaData.getModifiedTimeStamp().format(dateTimeFormatter);

            // Determine whether the object is the current directory, parent directory, or a regular file/directory
            String fileName = object.equals(currentDirectory) ? "." :
                    object.equals(parentDirectory) ? ".." : object.getName();

            // Calculate spaces needed for formatting
            String userNameSpaces = " ".repeat(maxUserNameLength - userName.length());
            String groupNameSpaces = " ".repeat(maxGroupNameLength - groupName.length());
            String sizeSpaces = " ".repeat(maxSizeLength - fileSize.length());

            // Format and append file information to the StringBuilder
            stringBuilder.append(permission).append(" ")
                    .append(userName).append(userNameSpaces).append(" ")
                    .append(groupName).append(groupNameSpaces).append(" ")
                    .append(fileSize).append(sizeSpaces).append(" ")
                    .append(timeStamp).append(" ")
                    .append(fileName).append(userNameSpaces).append("\n");
        }

        return stringBuilder.toString().strip();
    }
}
