



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResizingFrameTest {

    private static volatile int mouseEnteredCount = 0;
    private static volatile int mouseExitedCount = 0;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {

        Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.mouseMove(100, 100);
        robot.delay(200);

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });


        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 1 || mouseExitedCount != 0) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setExtendedState(Frame.ICONIFIED);
            }
        });

        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 1 || mouseExitedCount != 1) {
            throw new RuntimeException("No Mouse Entered/Exited events! "+mouseEnteredCount+", "+mouseExitedCount);
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setExtendedState(Frame.NORMAL);
            }
        });

        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 2 || mouseExitedCount != 1) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        robot.mouseMove(500, 500);
        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 2 || mouseExitedCount != 2) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        });

        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 3 || mouseExitedCount != 2) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }


        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setExtendedState(Frame.NORMAL);
            }
        });

        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 3 || mouseExitedCount != 3) {
            throw new RuntimeException("No Mouse Entered/Exited events!");

        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setLocation(400, 400);
            }
        });

        robot.waitForIdle();
        robot.delay(1000);

        if (mouseEnteredCount != 4 || mouseExitedCount != 3) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setLocation(100, 100);
            }
        });

        robot.waitForIdle();
        robot.delay(400);

        if (mouseEnteredCount != 4 || mouseExitedCount != 4) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setBounds(100, 100, 800, 800);
            }
        });

        robot.waitForIdle();
        robot.delay(200);

        if (mouseEnteredCount != 5 || mouseExitedCount != 4) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }

        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.setBounds(100, 100, 200, 300);
            }
        });

        robot.waitForIdle();
        robot.delay(400);


        if (mouseEnteredCount != 5 || mouseExitedCount != 5) {
            throw new RuntimeException("No Mouse Entered/Exited events!");
        }
    }

    private static void createAndShowGUI() {

        frame = new JFrame("Main Frame");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEnteredCount++;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseExitedCount++;
            }
        });

        frame.setVisible(true);
    }
}
