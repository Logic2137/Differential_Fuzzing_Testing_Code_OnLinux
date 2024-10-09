

import java.awt.*;
import java.awt.event.*;

public class MyCanvas extends Canvas {

    static {
        try {
            System.loadLibrary("mylib");
        } catch (Throwable t) {
            System.out.println("Test failed!!");
            t.printStackTrace();
            System.exit(1);
        }
    }

    public native void paint(Graphics g);

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Frame f = new Frame();
            f.setBounds(0, 0, 100, 100);
            f.add(new MyCanvas());
            f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    System.exit(0);
                }
            });
            f.setVisible(true);
            robot.delay(5000);
            Color col1 = new Color(0, 0, 0);
            Color col2 = robot.getPixelColor(f.getX()+50, f.getY()+50);
            if (col1.equals(col2)) {
                System.out.println("Test passed!");
            } else {
                throw new RuntimeException("Color of JAWT canvas is wrong or " +
                        "it was not rendered. " + "Check that other windows " +
                        "do not block the test frame.");
            }
            System.exit(0);
        } catch (Throwable t) {
            System.out.println("Test failed!");
            t.printStackTrace();
            System.exit(1);
        }
    }
}
