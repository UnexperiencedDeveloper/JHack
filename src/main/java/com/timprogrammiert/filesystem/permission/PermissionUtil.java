package com.timprogrammiert.filesystem.permission;

/**
 * @author tmatz
 */
public class PermissionUtil {
    public static FilePermission changePermission(FilePermission originalPermission, int permissionNumber){
        if(permissionNumber == 0){
            originalPermission.setPermissionString("---------");
        }else {
            int ownerPermission = Integer.parseInt(String.valueOf(permissionNumber).substring(0,1));
            int groupPermission = Integer.parseInt(String.valueOf(permissionNumber).substring(1,2));
            int otherPermission = Integer.parseInt(String.valueOf(permissionNumber).substring(2,3));
            String updatedPemString = determinePermission(ownerPermission) +
                    determinePermission(groupPermission) + determinePermission(otherPermission);
            originalPermission.setPermissionString(updatedPemString);
        }

        return originalPermission;
    }

    private static String determinePermission(int permissionNumber){
        return switch (permissionNumber) {
            case 0 -> "---";
            case 1 -> "--x";
            case 2 -> "-w-";
            case 3 -> "-wx";
            case 4 -> "r--";
            case 5 -> "r-x";
            case 6 -> "rw-";
            case 7 -> "rwx";
            default -> "";
        };
    }
}
