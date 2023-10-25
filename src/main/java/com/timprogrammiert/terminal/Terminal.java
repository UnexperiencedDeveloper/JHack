package com.timprogrammiert.terminal;

import com.timprogrammiert.filesystem.path.Path;
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
        run();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        String input;
        while(true){
            System.out.print(Path.getAbsolutePathByFileObject(host.getCurrentDirectory()) + " ");
            input = scanner.nextLine();
            commandParser.parseCommand(input.split(" "));
            if(input.equals("exit")) return;
        }
    }
}
