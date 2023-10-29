package com.timprogrammiert.filesystem;


import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.cat.CatCommand;
import com.timprogrammiert.commands.cd.CdCommand;
import com.timprogrammiert.commands.chmod.ChmodCommand;
import com.timprogrammiert.commands.echo.EchoCommand;
import com.timprogrammiert.commands.ls.LsCommand;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.executable.ExecutableFile;
import com.timprogrammiert.filesystem.permission.PermissionUtil;
import com.timprogrammiert.filesystem.regularFile.RegularFile;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.util.FileTypeEnum;

/**
 * @author tmatz
 */
public class VirtualFileSystem {
    private final Directory rootDirectory;
    private Directory homeDirectory;
    private final Host host;

    public VirtualFileSystem(Host host) {
        this.host = host;
        rootDirectory = Directory.createRootDirectory("/", FileTypeEnum.Directory, host.getRootUser());
        setupFilesystemStructure();
    }
    public Directory getRootDirectory(){
        return rootDirectory;
    }
    public Directory getHomeDirectory(){return homeDirectory;}

    public Directory getBinDirectory(){
        return (Directory) rootDirectory.getChildrenByName("bin");
    }
    public Directory getVarDirectory(){
        return null;
    }

    public Host getHost() {
        return host;
    }

    private void setupFilesystemStructure(){
        Directory bin = Directory.createDirectory("bin", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(bin);

        ICommand lsCommand = new LsCommand();
        ExecutableFile ls = ExecutableFile.createExecutable("ls",
                FileTypeEnum.Executable,
                host.getRootUser(),
                lsCommand,
                bin);
        bin.addNewChildren(ls);

        ICommand cdCommand = new CdCommand();
        ExecutableFile cd = ExecutableFile.createExecutable("cd",
                FileTypeEnum.Executable,
                host.getRootUser(),
                cdCommand,
                bin);
        bin.addNewChildren(cd);

        ICommand echoCommand = new EchoCommand();
        ExecutableFile echo = ExecutableFile.createExecutable("echo",
                FileTypeEnum.Executable,
                host.getRootUser(),
                echoCommand,
                bin);
        bin.addNewChildren(echo);

        ICommand catCommand = new CatCommand();
        ExecutableFile cat = ExecutableFile.createExecutable("cat",
                FileTypeEnum.Executable,
                host.getRootUser(),
                catCommand,
                bin);
        bin.addNewChildren(cat);

        ICommand chmodCommand = new ChmodCommand();
        ExecutableFile chmod = ExecutableFile.createExecutable("chmod",
                FileTypeEnum.Executable,
                host.getRootUser(),
                chmodCommand,
                bin);
        bin.addNewChildren(chmod);

        Directory etc = Directory.createDirectory("etc", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(etc);
        PermissionUtil.changePermission(etc.getFileMetaData().getFilePermission(), "775");

        RegularFile passwd = RegularFile.createRegularFile("passwd", FileTypeEnum.RegularFile, host.getRootUser(), etc);
        etc.addNewChildren(passwd);
        PermissionUtil.changePermission(passwd.getFileMetaData().getFilePermission(), "664");

        homeDirectory = Directory.createDirectory("home", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(homeDirectory);

        Directory usr = Directory.createDirectory("usr", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(usr);

        Directory usrBin = Directory.createDirectory("bin", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        usr.addNewChildren(usrBin);
        PermissionUtil.changePermission(usrBin.getFileMetaData().getFilePermission(), "770");

        Directory var = Directory.createDirectory("var", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(var);
        PermissionUtil.changePermission(var.getFileMetaData().getFilePermission(), "770");

        Directory tmp = Directory.createDirectory("tmp", FileTypeEnum.Directory, host.getRootUser(), rootDirectory);
        rootDirectory.addNewChildren(tmp);


    }
}
