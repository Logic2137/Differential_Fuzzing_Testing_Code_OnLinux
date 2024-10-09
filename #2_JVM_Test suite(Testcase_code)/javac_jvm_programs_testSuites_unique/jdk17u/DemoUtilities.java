
package com.sun.swingset3.demos;

import java.awt.*;
import java.net.URI;
import java.io.IOException;
import javax.swing.*;

public class DemoUtilities {

    private DemoUtilities() {
    }

    public static void setToplevelLocation(Window toplevel, Component component, int relativePosition) {
        Rectangle compBounds = component.getBounds();
        Point p = new Point();
        SwingUtilities.convertPointToScreen(p, component);
        int x;
        int y;
        switch(relativePosition) {
            case SwingConstants.NORTH:
                {
                    x = (p.x + (compBounds.width / 2)) - (toplevel.getWidth() / 2);
                    y = p.y - toplevel.getHeight();
                    break;
                }
            case SwingConstants.EAST:
                {
                    x = p.x + compBounds.width;
                    y = (p.y + (compBounds.height / 2)) - (toplevel.getHeight() / 2);
                    break;
                }
            case SwingConstants.SOUTH:
                {
                    x = (p.x + (compBounds.width / 2)) - (toplevel.getWidth() / 2);
                    y = p.y + compBounds.height;
                    break;
                }
            case SwingConstants.WEST:
                {
                    x = p.x - toplevel.getWidth();
                    y = (p.y + (compBounds.height / 2)) - (toplevel.getHeight() / 2);
                    break;
                }
            case SwingConstants.NORTH_EAST:
                {
                    x = p.x + compBounds.width;
                    y = p.y - toplevel.getHeight();
                    break;
                }
            case SwingConstants.NORTH_WEST:
                {
                    x = p.x - toplevel.getWidth();
                    y = p.y - toplevel.getHeight();
                    break;
                }
            case SwingConstants.SOUTH_EAST:
                {
                    x = p.x + compBounds.width;
                    y = p.y + compBounds.height;
                    break;
                }
            case SwingConstants.SOUTH_WEST:
                {
                    x = p.x - toplevel.getWidth();
                    y = p.y + compBounds.height;
                    break;
                }
            default:
            case SwingConstants.CENTER:
                {
                    x = (p.x + (compBounds.width / 2)) - (toplevel.getWidth() / 2);
                    y = (p.y + (compBounds.height / 2)) - (toplevel.getHeight() / 2);
                }
        }
        toplevel.setLocation(x, y);
    }

    public static boolean browse(URI uri) throws IOException {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(uri);
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }
}
