package com.timprogrammiert.filesystem.manager;

import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.regularFile.RegularFile;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.user.User;

/**
 * @author tmatz
 * @version 1.0
 */
public class EtcPasswdManager {
    private final Path pathToEtcPasswd = new Path("/etc/passwd");
    private final RegularFile etcPasswdFile;

    public EtcPasswdManager(Host host) {
        try {
            etcPasswdFile = pathToEtcPasswd.resolvePath(host, RegularFile.class);
        } catch (FileObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeNewUser(User user){
        StringBuilder contentString = new StringBuilder();
        contentString.append(user.getUserName())
                .append(":")
                .append("x").append(":")
                .append(user.getUid()).append(":")
                .append(user.getPrimaryGroup().getGid()).append(":")
                .append(Path.getAbsolutePathByFileObject(user.getHomeDirectory())).append("\n");
        etcPasswdFile.appendContent(contentString.toString());
    }
}
