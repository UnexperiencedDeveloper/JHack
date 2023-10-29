package com.timprogrammiert.commands.ls;

import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

/**
 * The `TabledPrinter` class is responsible for formatting and printing a tabular
 *  representation of a collection of `FileObject` items. It calculates the maximum
 *  width for each column (user name, group name, file size) and formats the details
 *  of each `FileObject` into a table with columns for permissions, user name, group
 *  name, file size, modification timestamp, and name. It then prints the formatted
 *  table to the console.
 *
 * @author tmatz
 * @version 1.0
 */
public class TabledPrinter {
    private int maxUserNameLength;
    private int maxSizeLength;
    private int maxGroupNameLength;
    private final Collection<FileObject> children;

    /**
     * Constructor for the `TabledPrinter` class. Initializes the class with the
     * collection of `FileObject` items to be printed.
     *
     * @param children A collection of `FileObject` items to be formatted and printed.
     */
    public TabledPrinter(Collection<FileObject> children){
        this.children = children;
    }

    /**
     * Calculates the maximum width for each column in the table by iterating
     * through the collection of `FileObject` items. It calculates the maximum
     * lengths for user names, group names, and file sizes.
     */
    private void calculateWidth(){
        // Calculate max Width for each Column
        maxUserNameLength = 0;
        maxSizeLength = 0;
        maxGroupNameLength = 0;
        for (FileObject object : children) {
            maxGroupNameLength = Math.max(maxGroupNameLength, object.getFileMetaData().getFilePermission().getUserGroup().getGroupName().length());
            maxUserNameLength = Math.max(maxUserNameLength, object.getFileMetaData().getFilePermission().getUser().getUserName().length());
            maxSizeLength = Math.max(maxSizeLength, String.valueOf(object.caluclateFileSize()).length());
        }
    }

    /**
     * Formats and prints the tabular representation of the `FileObject` items
     * to the console. It calculates column widths, formats the details of each
     * `FileObject`, and prints the formatted table. The table includes columns
     * for permissions, user name, group name, file size, modification timestamp,
     * and name.
     */
    public void printTabled(){
        StringBuilder stringBuilder = new StringBuilder();
        calculateWidth();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd HH:mm", Locale.ENGLISH);

        for (FileObject object : children) {
            // Append detailed file information: permissions, user, group, size, modification timestamp, and name
            FileMetaData metaData = object.getFileMetaData();
            String userNameSpaces = " ".repeat(maxUserNameLength - object.getFileMetaData().getFilePermission().getUser().getUserName().length());
            String groupNameSpaces = " ".repeat(maxGroupNameLength - object.getFileMetaData().getFilePermission().getUserGroup().getGroupName().length());
            String sizeSpaces = " ".repeat(maxSizeLength - String.valueOf(object.caluclateFileSize()).length());
            stringBuilder.append(metaData.getFilePermission().getPermissionString()).append(" ")
                    .append(" ").append(metaData.getFilePermission().getUser().getUserName()).append(userNameSpaces).append(" ")
                    .append(" ").append(metaData.getFilePermission().getUserGroup().getGroupName()).append(groupNameSpaces).append(" ")
                    .append(sizeSpaces).append(object.caluclateFileSize()).append(" ")
                    .append(metaData.getModifiedTimeStamp().format(dateTimeFormatter)).append(" ")
                    .append(object.getName()).append(userNameSpaces).append("\n");
        }
        if (!stringBuilder.toString().isEmpty()) {
            System.out.println(stringBuilder.toString().strip());
        }
    }
}
