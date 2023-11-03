package com.timprogrammiert.commands.ls;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The `TablePrinter` class is responsible for printing a tabular
 * representation of a collection of `FileObject` items. It utilizes a `TableFormatter`
 * object to format the data into columns, including permissions, user names, group
 * names, file sizes, modification timestamps, and names. The formatted table is then
 * printed to the console.
 * <p>
 * This class handles exceptions, such as `NullPointerException`, during the formatting
 * process and logs them as warnings using a `Logger`.
 *
 * @author tmatz
 * @version 1.0
 */
public class TablePrinter {
    private final static Logger logger = Logger.getLogger(TablePrinter.class.getName());
    private final TableFormatter tableFormatter;

    public TablePrinter(TableFormatter tableFormatter) {
        this.tableFormatter = tableFormatter;
    }
    public void printTable() throws NullPointerException{
        System.out.println(tableFormatter.getFormattedTable());

    }
}

