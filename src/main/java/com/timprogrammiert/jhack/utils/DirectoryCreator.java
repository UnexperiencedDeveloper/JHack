package com.timprogrammiert.jhack.utils;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.exceptions.PermissionDeniedException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.permissions.Permission;
import com.timprogrammiert.jhack.permissions.PermissionChecker;
import com.timprogrammiert.jhack.permissions.PermissionUtil;
import com.timprogrammiert.jhack.users.User;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class DirectoryCreator {
    /**
     * Creates a directory along the specified path in the file system.
     *
     * @param path          The path of the directory to be created.
     * @param parentFolder  The parent directory where the new directory should be created.
     * @param computer      An object representing the computer or system where the operation is performed.
     * @throws PermissionDeniedException If there is a permission issue during directory creation.
     */
    public static void createDirectory(String path, Directory parentFolder, Computer computer)
            throws PermissionDeniedException {
        // Split the path into segments using "/" as the delimiter
        for (String segment : path.split("/")) {
            if (!segment.isEmpty()) {
                // Get the existing file or directory with the given segment name in the current parentFolder
                BaseFile segmentObj = parentFolder.getChildByName(segment);

                if (segmentObj == null) {
                    // If the segment does not exist, check write and read permissions on the parentFolder
                    if (hasWriteAndReadPermission(parentFolder, computer)) {
                        // Create the directory segment if permissions are granted
                        parentFolder = createDirectorySegment(segment, parentFolder, computer);
                    } else {
                        // Throw a PermissionDeniedException if write and read permissions are not granted
                        throw new PermissionDeniedException("Permission denied for creating directory: " + segment);
                    }
                } else if (segmentObj instanceof Directory) {
                    // If the segment exists and is a Directory, update the parentFolder
                    parentFolder = (Directory) segmentObj;
                }
            }
        }
    }


    private static boolean hasWriteAndReadPermission(Directory directory, Computer computer) {
        User currentUser = computer.getOperatingSystem().getCurrentUser();
        PermissionChecker pemChecker = new PermissionChecker(directory.getPermission(), currentUser);
        return pemChecker.isCanWrite() && pemChecker.isCanRead();
    }

    private static Directory createDirectorySegment(String segment, Directory parentFolder, Computer computer) {
        User currentUser = computer.getOperatingSystem().getCurrentUser();
        Permission permission = PermissionUtil.createDefaultDirectoryPermission(
                currentUser, currentUser.getPrimaryGroup());

        // Create directory
        return new Directory(segment,parentFolder, permission);
    }
}
