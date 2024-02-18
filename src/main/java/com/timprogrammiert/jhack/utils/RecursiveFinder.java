package com.timprogrammiert.jhack.utils;

import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;

import java.util.Collection;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class RecursiveFinder {
    public static Collection<BaseFile> getAllRecursive(Directory startDirectory){
        return startDirectory.getChildrenRecursive();
    }
}
