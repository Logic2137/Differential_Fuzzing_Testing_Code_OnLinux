

package com.sun.swingset3.demos.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import javax.swing.JPanel;



public class BusyGlass extends JPanel {

    
    public BusyGlass() {
        setLayout(new BorderLayout());
        setVisible(false); 
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    protected void paintComponent(Graphics g) {
        
        
        
        Color bgColor = getBackground();
        g.setColor(new Color(bgColor.getRed(),
                bgColor.getGreen(),
                bgColor.getBlue(), 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

