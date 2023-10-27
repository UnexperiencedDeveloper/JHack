package com.timprogrammiert.filesystem.permission;

/**
 * @author tmatz
 */
public class PermissionUtil {
    public static FilePermission changePermission(FilePermission originalPermission, int permissionNumber){
        // permissionNumber can like in linux 700, 777, 655
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
        String pemString = "";
        switch (permissionNumber){
            case 0:
                pemString =  "---";
                break;
            case 1:
                pemString = "--x";
                break;
            case 2:
                pemString = "-w-";
                break;
            case 3:
                pemString = "-wx";
                break;
            case 4:
                pemString = "r--";
                break;
            case 5:
                pemString = "r-x";
                break;
            case 6:
                pemString = "rw-";
                break;
            case 7:
                pemString = "rwx";
                break;
        }
        return pemString;
    }
}
