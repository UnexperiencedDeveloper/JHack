package com.timprogrammiert.filesystem.permission;


/**
 *  The `PermissionUtil` class provides utility methods to handle and modify file permissions.
 *  It contains static methods for changing permission strings based on numeric representations,
 *  allowing for easy conversion between numerical permissions and their symbolic representations.
 * @author tmatz
 * @version 1.0
 */
public class PermissionUtil {
    /**
     * Changes the file permission based on the given numeric representation.
     *
     * @param originalPermission The original FilePermission object to be modified.
     * @param permissionNumber   The numeric representation of the permission (e.g., "755").
     * @return The updated FilePermission object after applying the numeric permissions.
     */
    public static FilePermission changePermission(FilePermission originalPermission, String permissionNumber){
        String ownerPermission = String.valueOf(permissionNumber).substring(0,1);
        String groupPermission = String.valueOf(permissionNumber).substring(1,2);
        String otherPermission = String.valueOf(permissionNumber).substring(2,3);
        String updatedPemString = determinePermission(ownerPermission) +
                determinePermission(groupPermission) + determinePermission(otherPermission);
        originalPermission.setPermissionString(updatedPemString);

        return originalPermission;
    }

    /**
     * Determines the symbolic representation of a numeric permission digit.
     *
     * @param permissionNumber The numeric permission digit (0 to 7).
     * @return The symbolic representation of the permission digit (e.g., "---", "rwx").
     */
    private static String determinePermission(String permissionNumber){
        return switch (permissionNumber) {
            case "0" -> "---";
            case "1" -> "--x";
            case "2"-> "-w-";
            case "3" -> "-wx";
            case "4" -> "r--";
            case "5" -> "r-x";
            case "6" -> "rw-";
            case "7" -> "rwx";
            default -> "";
        };
    }
}
