package com.timprogrammiert.filesystem.permission;

import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.user.User;

/**
 * @author tmatz
 */
public class PermissionChecker {
    private final FileObject fileObject;
    private final User currentUser;
    private boolean canRead;
    private boolean canWrite;
    private boolean canExecute;
    private final Host host;

    public PermissionChecker(FileObject fileObject, Host host) {
        this.fileObject = fileObject;
        this.host = host;
        this.currentUser = host.getCurrentUser();
        extractPermissions();
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public boolean isCanExecute() {
        return canExecute;
    }

    public boolean isCanRead() {
        return canRead;
    }

    private void extractPermissions(){
        String permissionString = fileObject.getFileMetaData().getFilePermission().getPermissionString();
        String ownerPermission = permissionString.substring(1,4);
        String groupPermission = permissionString.substring(4,7);
        String otherPermission = permissionString.substring(7,10);

        // Root always full access
        if(host.getCurrentUser().equals(host.getRootUser())) {
            setPermission("rwx");
            return;
        }
        if(currentUser.equals(fileObject.getFileMetaData().getFilePermission().getUser())){
            // User is owner
            setPermission(ownerPermission);
        } else if (currentUser.getPrimaryGroup().equals(fileObject.getFileMetaData().getFilePermission().getUserGroup())) {
            // User is member of Group
            setPermission(groupPermission);
        } else {
            // User is part of other Users
            setPermission(otherPermission);
        }
    }

    private void setPermission(String permissions){
        canRead = permissions.charAt(0) == 'r';
        canWrite = permissions.charAt(1) == 'w';
        canExecute = permissions.charAt(2) == 'x';
    }
}
