import java.awt.*;
import java.awt.event.*;

public class ToolkitPropertyTest_Enable extends Frame {

    static boolean propValue;

    static Robot robot;

    static int[] buttonsPressed;

    static int[] buttonsReleased;

    static int[] buttonsClicked;

    public static void main(String[] s) {
        propValue = Boolean.parseBoolean(System.getProperty("sun.awt.enableExtraMouseButtons"));
        buttonsPressed = new int[MouseInfo.getNumberOfButtons()];
        buttonsReleased = new int[MouseInfo.getNumberOfButtons()];
        buttonsClicked = new int[MouseInfo.getNumberOfButtons()];
        ToolkitPropertyTest_Enable frame = new ToolkitPropertyTest_Enable();
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
            boolean lessThenFourButtons = (MouseInfo.getNumberOfButtons() <= 3);
            if (!lessThenFourButtons) {
                frame.addMouseListener(ma1);
                testCase2();
                frame.removeMouseListener(ma1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void testCase0() {
        if (!propValue) {
            throw new RuntimeException("TEST FAILED (0) : System property sun.awt.enableExtraMouseButtons = " + propValue);
        }
    }

    public static void testCase1() {
        if (Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled() == false) {
            throw new RuntimeException("TEST FAILED (1) : setting to TRUE. enabled = " + Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled());
        }
    }

    public static void testCase2() {
        emptyArrays();
        int[] buttonMasks = new int[MouseInfo.getNumberOfButtons()];
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            buttonMasks[i] = InputEvent.getMaskForButton(i + 1);
            System.out.println("TEST: buttonMasks[" + i + "] = " + buttonMasks[i]);
        }
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            System.out.println("button to press = " + (i + 1) + " : value passed to robot = " + buttonMasks[i]);
            robot.mousePress(buttonMasks[i]);
            robot.delay(70);
            robot.mouseRelease(buttonMasks[i]);
            robot.delay(200);
        }
        robot.delay(1000);
        for (int i = 0; i < MouseInfo.getNumberOfButtons(); i++) {
            if (buttonsPressed[i] != 1 || buttonsReleased[i] != 1 || buttonsClicked[i] != 1) {
                throw new RuntimeException("TESTCASE 2 FAILED : button " + (i + 1) + " wasn't single pressed|released|clicked : " + buttonsPressed[i] + " : " + buttonsReleased[i] + " : " + buttonsClicked[i]);
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
