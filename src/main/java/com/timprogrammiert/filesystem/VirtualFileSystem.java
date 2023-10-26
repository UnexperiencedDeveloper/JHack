package com.timprogrammiert.filesystem;


import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.cd.CdCommand;
import com.timprogrammiert.commands.ls.LsCommand;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.executable.ExecutableFile;
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
        rootDirectory = Directory.createRootDirectory("/", FileType.Directory, host.getRootUser());
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
        Directory bin = Directory.createDirectory("bin", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(bin);

        ICommand lsCommand = new LsCommand();
        ExecutableFile ls = ExecutableFile.createExecutable("ls",
                FileType.Executable,
                host.getRootUser(),
                lsCommand,
                bin);
        bin.addNewChildren(ls);

        ICommand cdCommand = new CdCommand();
        ExecutableFile cd = ExecutableFile.createExecutable("cd",
                FileType.Executable,
                host.getRootUser(),
                cdCommand,
                bin);
        bin.addNewChildren(cd);


        Directory etc = Directory.createDirectory("etc", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(etc);

        Directory home = Directory.createDirectory("home", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(home);

        Directory usr = Directory.createDirectory("usr", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(usr);

        Directory usrBin = Directory.createDirectory("bin", FileType.Directory, host.getRootUser(), rootDirectory);
        usr.addNewChildren(usrBin);

        Directory var = Directory.createDirectory("var", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(var);

        Directory tmp = Directory.createDirectory("tmp", FileType.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(tmp);

        Directory test = Directory.createDirectory("test", FileType.Directory, host.getRootUser(), tmp);
        tmp.addNewChildren(test);


    }
}
