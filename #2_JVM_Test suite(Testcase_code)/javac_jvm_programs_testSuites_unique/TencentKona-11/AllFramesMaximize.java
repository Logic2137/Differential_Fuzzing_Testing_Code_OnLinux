



import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AllFramesMaximize {
    private static JButton passButton;
    private static JButton failButton;
    private static JTextArea instructions;
    private static JFrame mainFrame;
    private static JFrame instructionFrame;
    public static boolean isProgInterruption = false;
    static Thread mainThread = null;
    static int sleepTime = 300000;

    public static void createAndShowJFrame() {
        passButton = new JButton("Pass");
        passButton.setEnabled(true);

        failButton = new JButton("Fail");
        failButton.setEnabled(true);

        instructions = new JTextArea(8, 30);
        instructions.setText(" This is a manual test\n\n" +
                " 1) Click on the maximize button, JFrame will enter fullscreen\n" +
                " 2) Click anywhere on the JFrame\n" +
                " 3) Press Pass if new JFrame didn't open in fullscreen,\n" +
                " 4) Press Fail if new JFrame opened in fullscreen");

        instructionFrame = new JFrame("Test Instructions");
        instructionFrame.setLocationRelativeTo(null);
        instructionFrame.add(passButton);
        instructionFrame.add(failButton);
        instructionFrame.add(instructions);
        instructionFrame.setSize(200,200);
        instructionFrame.setLayout(new FlowLayout());
        instructionFrame.pack();
        instructionFrame.setVisible(true);

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                isProgInterruption = true;
                mainThread.interrupt();
            }
        });

        failButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                isProgInterruption = true;
                mainThread.interrupt();
                throw new RuntimeException("New JFrame opened on a new window!");
            }
        });

        mainFrame = new JFrame();
        JButton button = new JButton("Open Frame");
        mainFrame.getContentPane().add(button);
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame f = new JFrame();
                        f.setSize(400, 400);
                        f.setVisible(true);
                    }
                });
        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }

    private static void dispose() {
        mainFrame.dispose();
        instructionFrame.dispose();
    }

    public static void main(String[] args) throws Exception {
        mainThread = Thread.currentThread();
        SwingUtilities.invokeAndWait(AllFramesMaximize::createAndShowJFrame);

        try {
            mainThread.sleep(sleepTime);
        } catch (InterruptedException e) {
        if (!isProgInterruption) {
            throw e;
        }
        } finally {
            SwingUtilities.invokeAndWait(AllFramesMaximize::dispose);
        }

        if (!isProgInterruption) {
        throw new RuntimeException("Timed out after " + sleepTime / 1000
                + " seconds");
        }
    }
}

