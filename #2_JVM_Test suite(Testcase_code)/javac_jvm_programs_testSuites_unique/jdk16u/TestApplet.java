

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.String;
import java.lang.System;


public class TestApplet extends JApplet {

    public void init() {
        final TextArea log = new TextArea("Events:\n");
        log.setEditable(false);
        log.setSize(400, 200);
        this.add(log);
        log.addKeyListener(
                new KeyAdapter() {
                    @Override public void keyTyped(KeyEvent e) {
                        log.append("Key typed: char = " + e.getKeyChar() + "\n");
                    }

                    @Override public void keyPressed(KeyEvent e) {
                        log.append("Key pressed: char = " + e.getKeyChar() + " code = " + e.getKeyCode() + "\n");
                    }

                    @Override public void keyReleased(KeyEvent e) {
                        log.append("Key released: char = " + e.getKeyChar() + " code = " + e.getKeyCode() + "\n");
                    }
                });
    }

    public void start() {
    }

    public void stop() {
    }

    public void destroy() {
    }

}
