



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConsumeForModalDialogTest {
    Robot robot;
    JFrame frame = new JFrame("Test Frame");
    JDialog dialog = new JDialog((Window)null, "Test Dialog", Dialog.ModalityType.DOCUMENT_MODAL);
    JTextField text = new JTextField();
    static boolean passed = true;

    public static void main(String[] args) {
        ConsumeForModalDialogTest app = new ConsumeForModalDialogTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
    }

    public void start() {

        text.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    System.out.println(e.toString());
                    passed = false;
                }
            });

        JMenuItem testItem = new JMenuItem();
        testItem.setMnemonic('s');
        testItem.setText("Test");

        testItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dialog.setVisible(true);
            }
        });

        JMenu menu = new JMenu();
        menu.setMnemonic('f');
        menu.setText("File");
        menu.add(testItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        dialog.setSize(100, 100);
        dialog.add(text);

        frame.setJMenuBar(menuBar);
        frame.setSize(100, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        robot.waitForIdle();

        if (!frame.isFocusOwner()) {
            Point loc = frame.getLocationOnScreen();
            Dimension size = frame.getSize();
            robot.mouseMove(loc.x + size.width/2, loc.y + size.height/2);
            robot.delay(10);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.delay(10);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);

            robot.waitForIdle();

            int iter = 10;
            while (!frame.isFocusOwner() && iter-- > 0) {
                robot.delay(200);
            }
            if (iter <= 0) {
                System.out.println("Test: the frame couldn't be focused!");
                return;
            }
        }

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_ALT);

        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_S);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_S);

        robot.delay(1000);

        if (passed) {
            System.out.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed! Enexpected KeyTyped came into the JTextField.");
        }
    }
}
