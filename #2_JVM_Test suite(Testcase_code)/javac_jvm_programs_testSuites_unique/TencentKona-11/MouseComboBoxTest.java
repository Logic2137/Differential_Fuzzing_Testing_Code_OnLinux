


import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MouseComboBoxTest {
    private static final String[] items = {"One", "Two", "Three", "Four", "Five"};

    private static Robot robot = null;
    private static JFrame frame = null;
    private static JComboBox comboBox = null;
    private static MyComboBoxUI comboBoxUI = null;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(50);

        UIManager.setLookAndFeel(new MetalLookAndFeel());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        robot.waitForIdle();

        for (int i = 0; i < items.length; i++) {
            
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.waitForIdle();

            Point point = getItemPointToClick(i);
            robot.mouseMove(point.x, point.y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();

            if (i != getSelectedIndex()) {
                throw new RuntimeException("Test Failed! Incorrect value of selected index = " + getSelectedIndex() +
                        ", expected value = " + i);
            }
        }
    }

    private static Point getItemPointToClick(final int item) throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                BasicComboPopup popup = (BasicComboPopup)comboBoxUI.getComboPopup();
                Point point = popup.getLocationOnScreen();
                Dimension size = popup.getSize();

                int step = size.height / items.length;
                point.x += size.width / 2;
                point.y += step / 2 + step * item;
                result[0] = point;
            }
        });
        return result[0];
    }

    private static int getSelectedIndex() throws Exception {
        final int[] result = new int[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                result[0] = comboBox.getSelectedIndex();
            }
        });
        return result[0];
    }

    private static void createAndShowGUI() {
        frame = new JFrame("MouseComboBoxTest");

        comboBox = new JComboBox(items);
        comboBox.setEditable(true);
        comboBoxUI = new MyComboBoxUI();
        comboBox.setUI(comboBoxUI);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JWindow window = new JWindow(frame);
        window.add(comboBox);
        window.pack();
        window.setVisible(true);
    }

    private static class MyComboBoxUI extends MetalComboBoxUI {
        public ComboPopup getComboPopup() {
            return popup;
        }
    }
}

