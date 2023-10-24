package com.timprogrammiert.filesystem;


import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class VirtualFileSystem {
    private final Directory rootDirectory;
    private final Host host;

    public VirtualFileSystem(Host host) {
        this.host = host;
        rootDirectory = Directory.createRootDirectory("/", FileType.Directory, host.getCurrentUser());
        setupFilesystemStructure();
    }

    public Directory getRootDirectory(){
        return rootDirectory;
    }

    public Directory getBinDirectory(){
        return (Directory) rootDirectory.getChildrenByName("bin");
    }
    public Directory getVarDirectory(){
        return null;
    }

    private void setupFilesystemStructure(){
        Directory bin = Directory.createDirectory("bin", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(bin);

        Directory etc = Directory.createDirectory("etc", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(etc);

        Directory home = Directory.createDirectory("home", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(home);

        Directory usr = Directory.createDirectory("usr", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(usr);

        Directory usrBin = Directory.createDirectory("bin", FileType.Directory, host.getCurrentUser(), rootDirectory);
        usr.addNewChildren(usrBin);

        Directory var = Directory.createDirectory("var", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(var);

        Directory tmp = Directory.createDirectory("tmp", FileType.Directory, host.getCurrentUser(), rootDirectory);
        rootDirectory.addNewChildren(tmp);

        Directory test = Directory.createDirectory("test", FileType.Directory, host.getCurrentUser(), tmp);
        tmp.addNewChildren(test);


    }
}
