

import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



public class bug8021253 {

    private static volatile boolean defaultKeyPressed;
    private static JFileChooser fileChooser;
    private static File file;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100);

            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });

            robot.waitForIdle();
            robot.delay(1000);

            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    fileChooser.setSelectedFile(file);
                }
            });

            robot.waitForIdle();

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.waitForIdle();

            if (!defaultKeyPressed) {
                throw new RuntimeException("Default button is not pressed");
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }

    private static void createAndShowGUI() {

        file = getTempFile();

        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 300);

        fileChooser = new JFileChooser(file.getParentFile());
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultKeyPressed = true;
                frame.dispose();
            }
        });

        frame.getContentPane().add(BorderLayout.CENTER, fileChooser);
        frame.setSize(fileChooser.getPreferredSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static File getTempFile() {
        try {
            File temp = File.createTempFile("test", ".txt");
            temp.deleteOnExit();
            return temp;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
