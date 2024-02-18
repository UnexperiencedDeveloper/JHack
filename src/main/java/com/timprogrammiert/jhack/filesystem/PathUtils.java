package com.timprogrammiert.jhack.filesystem;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class PathUtils {
    public static String pathToString(BaseFile baseFile){
        StringBuilder pathString = new StringBuilder();

        // Build the directory string by traversing parent folders
        while (baseFile.getParent() != null) {
            pathString.insert(0, baseFile.name + "/");
            baseFile = baseFile.getParent();
        }

        // Insert the root directory separator
        pathString.insert(0, "/");
        return pathString.toString();
    }
}
