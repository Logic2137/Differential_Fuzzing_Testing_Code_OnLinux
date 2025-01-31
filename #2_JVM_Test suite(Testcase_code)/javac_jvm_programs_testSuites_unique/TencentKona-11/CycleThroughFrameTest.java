



import java.awt.Frame;
import java.awt.Button;
import java.awt.TextArea;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CycleThroughFrameTest {

    public static final int maxFrames = 5;
    private static JFrame[] frame;
    private static Frame instructionFrame;
    private static volatile boolean testContinueFlag = true;

    private static final String TEST_INSTRUCTIONS =
        " This is a manual test\n\n" +
        " 1) Configure Keyboard shortcut if not done in your system:\n" +
        " 2) Open System Preferences, go to -> Keyboard -> Shortcuts -> Keyboard\n" +
        " 3) Enable 'Move focus to next window' if disabled\n" +
        " 4) Enable 'Move focus to next window drawer' if disabled\n" +
        " 5) Close System Preferences\n" +
        " 5) Press COMMAND + ` keys to cycle through frames in forward order\n" +
        " 6) Press FAIL if focus doesn't move to next frame\n" +
        " 7) Press COMMAND + SHIFT + ` to cycle through frames in reverse order\n" +
        " 8) Press FAIL if focus doesn't move to next frame in reverse order\n" +
        " 9) Press PASS otherwise";

    private static final String FAIL_MESSAGE = "Focus doesn't move to next frame";

    public void showJFrame(int frameNumber) {

        String title = "Frame " + frameNumber;
        frame[frameNumber] = new JFrame(title);
        frame[frameNumber].setSize(300, 200);
        frame[frameNumber].setLocation(50+(frameNumber*20), 50+(frameNumber*20));
        frame[frameNumber].setVisible(true);
    }

    private void createAndShowFrame() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame = new JFrame[maxFrames];
                for (int i = 0; i < maxFrames; i++) {
                    showJFrame(i);
                }
            }
        });
    }

    public void createAndShowInstructionFrame() {
        Button passButton = new Button("Pass");
        passButton.setEnabled(true);

        Button failButton = new Button("Fail");
        failButton.setEnabled(true);

        TextArea instructions = new TextArea(12, 70);
        instructions.setText(TEST_INSTRUCTIONS);

        instructionFrame = new Frame("Test Instructions");
        instructionFrame.add(passButton);
        instructionFrame.add(failButton);
        instructionFrame.add(instructions);
        instructionFrame.setSize(200,200);
        instructionFrame.setLayout(new FlowLayout());
        instructionFrame.pack();
        instructionFrame.setVisible(true);

        passButton.addActionListener(ae -> {
            dispose();
            testContinueFlag = false;
        });

        failButton.addActionListener(ae -> {
            dispose();
            testContinueFlag = false;
            throw new RuntimeException(FAIL_MESSAGE);
        });
    }

    private static void dispose() {
        for (int i = 0; i < maxFrames; i++) {
            frame[i].dispose();
        }
        instructionFrame.dispose();
    }

    public static void main(String[] args)  throws Exception {

        CycleThroughFrameTest testObj = new CycleThroughFrameTest();
        testObj.createAndShowFrame();
        testObj.createAndShowInstructionFrame();

        final int sleepTime = 300000;
        final int sleepLoopTime = 1000;
        int remainingSleepTime = sleepTime;
        while(remainingSleepTime > 0 && testContinueFlag) {
            Thread.sleep(sleepLoopTime);
            remainingSleepTime -= sleepLoopTime;
        }

        if (testContinueFlag) {
            dispose();
            throw new RuntimeException("Timed out after " +
                    (sleepTime - remainingSleepTime) / 1000 + " seconds");
        }
    }
}

