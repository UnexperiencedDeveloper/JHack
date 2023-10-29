package com.timprogrammiert.terminal;

import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.host.Host;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmatz
 */
public class Terminal {
    private Host host;
    private CommandParser commandParser;
    private static final Logger logger = Logger.getLogger(Terminal.class.getName());
    public Terminal() {
        host = new Host("Tims Pc");
        commandParser = new CommandParser(host);
        run();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        logger.log(Level.INFO, "Terminal started");

        String input;
        while(true){
            System.out.print(Path.getAbsolutePathByFileObject(host.getCurrentDirectory()) + " ");
            input = scanner.nextLine();
            commandParser.parseCommand(input.split(" "));
            if(input.equals("exit")) return;
        }
    }
}
