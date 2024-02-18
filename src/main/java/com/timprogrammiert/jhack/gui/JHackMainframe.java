package com.timprogrammiert.jhack.gui;

import com.timprogrammiert.jhack.gui.components.TerminalTextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class JHackMainframe extends VBox {
    Stage stage;
    public JHackMainframe(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init(){
        TerminalTextArea terminal = new TerminalTextArea();
        terminal.prefHeightProperty().bind(stage.heightProperty());
        getChildren().add(terminal);
    }

}
