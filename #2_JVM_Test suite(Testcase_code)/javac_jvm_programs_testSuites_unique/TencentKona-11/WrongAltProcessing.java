



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class WrongAltProcessing {

    private static Robot robot;
    private static JFrame firstFrame;
    private static JFrame secondFrame;
    private static JTextField mainFrameTf1;
    private static JTextField mainFrameTf2;
    private static JTextField secondFrameTf;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            return;
        }
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                createWindows();
            }
        });
        initRobot();
        robot.waitForIdle();
        runScript();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                verify();
            }
        });
    }

    private static void verify() {
        Component c = DefaultKeyboardFocusManager
                .getCurrentKeyboardFocusManager().getFocusOwner();
        if (!(c == mainFrameTf2)) {
            throw new RuntimeException("Wrong focus owner.");
        }
    }

    public static void initRobot() throws AWTException {
        robot = new Robot();
        robot.setAutoDelay(100);
    }

    private static void clickWindowsTitle(JFrame frame) {
        Point point = frame.getLocationOnScreen();
        robot.mouseMove(point.x + (frame.getWidth() / 2), point.y + frame.getInsets().top / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void runScript() {
        robot.delay(1000);
        printABCD();
        pressTab();
        clickWindowsTitle(secondFrame);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_ALT);
        clickWindowsTitle(firstFrame);
        robot.waitForIdle();
    }

    private static void pressTab() {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
    }

    private static void printABCD() {
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_B);
        robot.keyRelease(KeyEvent.VK_B);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_D);
    }

    public static void createWindows() {
        firstFrame = new JFrame("Frame");
        firstFrame.setLayout(new FlowLayout());
        firstFrame.setPreferredSize(new Dimension(600,100));

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Save");

        mainFrameTf1 = new JTextField(10);
        mainFrameTf2 = new JTextField(10);

        mainFrameTf1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent EVT) {
                if (EVT.getKeyChar() >= 'a' && EVT.getKeyChar() <= 'z') {
                    try {
                        
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        menu.add(item);
        bar.add(menu);
        firstFrame.setJMenuBar(bar);


        firstFrame.add(mainFrameTf1);
        firstFrame.add(mainFrameTf2);

        firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        firstFrame.pack();

        secondFrame = new JFrame("Frame 2");
        secondFrame.setPreferredSize(new Dimension(600,100));
        secondFrame.setLocation(0, 150);
        secondFrameTf = new JTextField(20);
        secondFrame.add(secondFrameTf);
        secondFrame.pack();

        secondFrame.setVisible(true);

        firstFrame.setVisible(true);

        mainFrameTf1.requestFocus();
    }
}
