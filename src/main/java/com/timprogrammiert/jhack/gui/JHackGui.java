package com.timprogrammiert.jhack.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class JHackGui {
    private final static String TITLE = "JHack";
    private final static int WIDTH = 600;
    private final static int HEIGHT = 400;
    private static JHackMainframe mainframe;
    Stage stage;
    public JHackGui(Stage stage) {
        this.stage = stage;
        init();
    }
    private void init(){
        JHackMainframe mainframe = new JHackMainframe(stage);
        JHackGui.mainframe = mainframe;
        stage.setTitle("JHack");
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setScene(new Scene(mainframe));
        stage.show();
    }

    public Stage getStage(){
        return stage;
    }

    public static JHackMainframe getMainframe(){
        return mainframe;
    }
}
