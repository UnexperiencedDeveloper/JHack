package com.timprogrammiert.jhack.gui.components;

import com.timprogrammiert.jhack.listeners.TerminalInputListener;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class TerminalTextArea extends TextArea {
    private static Logger logger = LoggerFactory.getLogger(TerminalInputListener.class);
    private String lastInput;
    public TerminalTextArea() {
        setFont(Font.font("Lucida Console", FontPosture.REGULAR, 14));
        lastInput = "";
        setupListener();
    }

    private void setupListener(){
        TerminalInputListener listener = new TerminalInputListener(this);
        addEventFilter(KeyEvent.KEY_PRESSED, listener);
        textProperty().addListener((observableValue, oldValue, newValue) -> {
             lastInput = newValue;
        });
    }
    public String[] getLastInput(){
        String[] lines = lastInput.split("\n");
        if(lines.length > 0){
            String[] lastLine = lines[lines.length - 1].split(" ");
            return removeBlankElements(Arrays.copyOfRange(lastLine, 1, lastLine.length));
        }else {
            return new String[0];
        }
    }

    private String[] removeBlankElements(String[] array){
        return Arrays.stream(array)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }
}
