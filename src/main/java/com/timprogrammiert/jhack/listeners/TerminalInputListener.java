package com.timprogrammiert.jhack.listeners;

import com.timprogrammiert.jhack.gui.components.TerminalTextArea;
import com.timprogrammiert.jhack.terminal.CommandHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class TerminalInputListener implements EventHandler<KeyEvent> {
    private static Logger logger = LoggerFactory.getLogger(TerminalInputListener.class);
    TerminalTextArea terminalTextArea;
    CommandHandler commandHandler;

    public TerminalInputListener(TerminalTextArea terminalTextArea) {
        this.terminalTextArea = terminalTextArea;
        commandHandler = new CommandHandler(terminalTextArea);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            // Enter Pressed
            commandHandler.handleInput(terminalTextArea.getLastInput());
            keyEvent.consume();
        } else if (keyEvent.getCode().equals(KeyCode.UP) ||
                (keyEvent.getCode().equals(KeyCode.DOWN))){
            keyEvent.consume();
        } else if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            MoveCarretLeft();
            keyEvent.consume();
        }else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            MoveCarretRight();
            keyEvent.consume();
        }
    }

    private void MoveCarretLeft(){
        int position = terminalTextArea.getCaretPosition();
        if((terminalTextArea.getText().charAt(position -1) != '\n')){
            position--;
        }
        terminalTextArea.positionCaret(position);
    }

    private void MoveCarretRight(){
        int position = terminalTextArea.getCaretPosition();
        try {
            if((terminalTextArea.getText().charAt(position) != '\n')){
                position++;
            }
            terminalTextArea.positionCaret(position);
        }catch (IndexOutOfBoundsException e){
            logger.debug(e.getMessage());
        }
    }
}
