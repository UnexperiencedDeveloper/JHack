package com.timprogrammiert.terminal;

import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.util.DirectoryUtil;
import com.timprogrammiert.host.Host;

import java.util.Scanner;

/**
 * @author tmatz
 */
public class Terminal {
    private Host host;
    private CommandParser commandParser;
    public Terminal() {
        host = new Host("Tims Pc");
        commandParser = new CommandParser(host);
        // Debug
        Path path = new Path("tmp/test");
        System.out.println(path.resolvePath(host, Directory.class).getFileType());
        //System.out.println(DirectoryUtil.resolvePath(host, Directory.class, new Path("tmp/test")).getFileType());
        run();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while(true){
            System.out.print(Path.getAbsolutePathByFileObject(host.getCurrentDirectory()) + " ");
            input = scanner.nextLine();
            commandParser.parseCommand(input.split(" "));
            if(input.equals("exit")) return;
        }
    }
}
