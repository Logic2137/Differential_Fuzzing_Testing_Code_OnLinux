import java.awt.*;
import java.awt.event.*;

public class ToolkitPropertyTest_Disable extends Frame {

    static boolean propValue;

    static Robot robot;

    static int[] buttonsPressed;

    static int[] buttonsReleased;

    static int[] buttonsClicked;

    static boolean lessThenFourButtons;

    public static void main(String[] s) {
        propValue = Boolean.parseBoolean(System.getProperty("sun.awt.enableExtraMouseButtons"));
        buttonsPressed = new int[MouseInfo.getNumberOfButtons()];
        buttonsReleased = new int[MouseInfo.getNumberOfButtons()];
        buttonsClicked = new int[MouseInfo.getNumberOfButtons()];
        ToolkitPropertyTest_Disable frame = new ToolkitPropertyTest_Disable();
        frame.setSize(300, 300);
        frame.setVisible(true);
        MouseAdapter ma1 = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                buttonsPressed[e.getButton() - 1] += 1;
                System.out.println("PRESSED " + e);
            }

            public void mouseReleased(MouseEvent e) {
                buttonsReleased[e.getButton() - 1] += 1;
                System.out.println("RELEASED " + e);
            }

            public void mouseClicked(MouseEvent e) {
                buttonsClicked[e.getButton() - 1] += 1;
                System.out.println("CLICKED " + e);
            }
        };
        try {
            robot = new Robot();
            robot.delay(1000);
            robot.mouseMove(frame.getLocationOnScreen().x + frame.getWidth() / 2, frame.getLocationOnScreen().y + frame.getHeight() / 2);
            System.out.println("Property = " + propValue);
            testCase0();
            testCase1();
            System.out.println("Number Of Buttons = " + MouseInfo.getNumberOfButtons());
            lessThenFourButtons = (MouseInfo.getNumberOfButtons() <= 3);
            if (!lessThenFourButtons) {
                frame.addMouseListener(ma1);
                testCase2();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void testCase0() {
        if (propValue) {
            throw new RuntimeException("TEST FAILED (0): System property sun.awt.enableExtraMouseButtons = " + propValue);
        }
    }

    public static void testCase1() {
        if (Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled() == true) {
            throw new RuntimeException("TEST FAILED (1): setting to FALSE. Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled() = " + Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled());
        }
    }

    public static void testCase2() {
        emptyArrays();
        int[] buttonMasks = new int[MouseInfo.getNumberOfButtons()];
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            buttonMasks[i] = InputEvent.getMaskForButton(i + 1);
            System.out.println("TEST: " + buttonMasks[i]);
        }
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            System.out.println("button to press = " + (i + 1) + " : value passed to robot = " + buttonMasks[i]);
            try {
                robot.mousePress(buttonMasks[i]);
                robot.delay(70);
                robot.mouseRelease(buttonMasks[i]);
                robot.delay(200);
                if (i >= 3) {
                    throw new RuntimeException("TESTCASE 2 FAILED : robot accepted the extra button " + (i + 1) + " instead of throwing an exception.");
                }
            } catch (IllegalArgumentException e) {
                if (i >= 3) {
                    System.out.println("Passed: an exception caught for extra button.");
                } else {
                    throw new RuntimeException("TESTCASE 2 FAILED : exception happen on standard button.", e);
                }
            }
        }
        robot.delay(2000);
        if (MouseInfo.getNumberOfButtons() < 3) {
            for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
                if (buttonsPressed[i] != 1 || buttonsReleased[i] != 1 || buttonsClicked[i] != 1) {
                    throw new RuntimeException("TESTCASE 2 FAILED : button " + (i + 1) + " wasn't single pressed.");
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (buttonsPressed[i] != 1 || buttonsReleased[i] != 1 || buttonsClicked[i] != 1) {
                    throw new RuntimeException("TESTCASE 2 FAILED : button " + (i + 1) + " wasn't single pressed.");
                }
            }
            for (int i = 3; i < MouseInfo.getNumberOfButtons(); i++) {
                if (buttonsPressed[i] != 0 || buttonsReleased[i] != 0 || buttonsClicked[i] != 0) {
                    throw new RuntimeException("TESTCASE 2 FAILED : button " + (i + 1) + " was pressed.");
                }
            }
        }
    }

    public static void emptyArrays() {
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            buttonsPressed[i] = 0;
            buttonsReleased[i] = 0;
            buttonsClicked[i] = 0;
        }
    }
}
