package com.timprogrammiert.filesystem.permission;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmatz
 */
public class PermissionUtil {
    private final static Logger logger = Logger.getLogger(PermissionUtil.class.getName());
    public static FilePermission changePermission(FilePermission originalPermission, String permissionNumber){
        String ownerPermission = String.valueOf(permissionNumber).substring(0,1);
        String groupPermission = String.valueOf(permissionNumber).substring(1,2);
        String otherPermission = String.valueOf(permissionNumber).substring(2,3);
        String updatedPemString = determinePermission(ownerPermission) +
                determinePermission(groupPermission) + determinePermission(otherPermission);
        originalPermission.setPermissionString(updatedPemString);

        return originalPermission;
    }

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
