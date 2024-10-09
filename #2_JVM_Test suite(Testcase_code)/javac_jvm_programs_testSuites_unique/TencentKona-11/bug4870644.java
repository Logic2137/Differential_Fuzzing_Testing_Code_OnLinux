

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class bug4870644 {
    JButton b1, b2, b3;
    JFrame frame;
    JMenu menu;
    JPopupMenu popup;
    static Robot robot;
    static boolean passed = true;
    private volatile Point p = null;
    private volatile Dimension d = null;
    void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                    d = comp.getSize();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            try {
                SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
                System.out.println("Test for LookAndFeel " + laf.getClassName());
                new bug4870644();
                System.out.println("Test passed for LookAndFeel " + laf.getClassName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public bug4870644() throws Exception {

        SwingUtilities.invokeAndWait(() -> {
            JMenuBar menuBar = new JMenuBar();
            menu = new JMenu("Menu");
            menuBar.add(menu);
            JMenuItem menuItem = new JMenuItem("Item");
            menu.add(menuItem);
            menu.add(new JMenuItem("Item 2"));
            frame = new JFrame("test");
            frame.setJMenuBar(menuBar);

            b1 = new JButton("One");
            b2 = new JButton("Two");
            b3 = new JButton("Default");
            b3.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    passed = false;
                }
            });
            frame.getContentPane().add(b1, BorderLayout.NORTH);
            frame.getContentPane().add(b2, BorderLayout.CENTER);
            frame.getContentPane().add(b3, BorderLayout.SOUTH);
            frame.getRootPane().setDefaultButton(b3);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });

        blockTillDisplayed(b1);
        robot.waitForIdle();

        robot.delay(500);
        robot.mouseMove(p.x + d.width-1, p.y + d.height/2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_F10);
        robot.keyRelease(KeyEvent.VK_F10);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> {
            popup = menu.getPopupMenu();
        });

        blockTillDisplayed(popup);
        robot.waitForIdle();
        robot.mouseMove(p.x + d.width-1, p.y + d.height/2);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> frame.dispose());
        if(!passed) {
            String cause = "Default button reacted on \"ctrl ENTER\" while menu is active.";
            throw new RuntimeException(cause);
        }
    }
}
