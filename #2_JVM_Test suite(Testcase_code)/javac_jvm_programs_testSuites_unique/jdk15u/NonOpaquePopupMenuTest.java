


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import static javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

public class NonOpaquePopupMenuTest extends JFrame {

    private static JMenu fileMenu;
    private static final String AQUALAF="com.apple.laf.AquaLookAndFeel";

    public NonOpaquePopupMenuTest() {
        getContentPane().setBackground(java.awt.Color.RED);
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("New");
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        fileMenu.add(menuItem);
        fileMenu.getPopupMenu().setOpaque(false);

        setSize(new Dimension(640, 480));
        setVisible(true);
    }

    public static void main(String[] args) throws Throwable {
        LookAndFeelInfo[] lookAndFeelInfoArray = getInstalledLookAndFeels();

        for (LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfoArray) {
            System.out.println(lookAndFeelInfo.getClassName());
            if ( AQUALAF == lookAndFeelInfo.getClassName()) {
                System.out.println("This test scenario is not applicable for" +
                        " Aqua LookandFeel and hence skipping the validation");
                continue;
            }
            setLookAndFeel(lookAndFeelInfo.getClassName());
            Robot robot = new Robot();
            robot.setAutoDelay(250);

            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    new NonOpaquePopupMenuTest();
                }
            });

            robot.waitForIdle();

            Point p = getMenuClickPoint();
            robot.mouseMove(p.x, p.y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            robot.waitForIdle();

            if (isParentOpaque()) {
                throw new RuntimeException("Popup menu parent is opaque");
            }
        }
    }

    private static boolean isParentOpaque() throws Exception {
        final boolean result[] = new boolean[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                result[0] = fileMenu.getPopupMenu().getParent().isOpaque();
            }
        });

        return result[0];
    }

    private static Point getMenuClickPoint() throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = fileMenu.getLocationOnScreen();
                Dimension size = fileMenu.getSize();

                result[0] = new Point(p.x + size.width / 2,
                        p.y + size.height / 2);
            }
        });

        return result[0];

    }
}
