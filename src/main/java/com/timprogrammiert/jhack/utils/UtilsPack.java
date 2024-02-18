package com.timprogrammiert.jhack.utils;

import javafx.scene.text.Font;

import java.util.List;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class UtilsPack {
    public static void PrintAllFontFamilies(){
        String[] fonts = Font.getFamilies().toArray(new String[0]);
        for (String font: fonts){
            System.out.println(font);
        }
    }
}
