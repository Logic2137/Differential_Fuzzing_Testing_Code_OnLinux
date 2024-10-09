

 
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class Test8158478 {

    public static void main(String args[]) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        X11KeysymTest test = new X11KeysymTest(latch);
        Thread T1 = new Thread(test);
        T1.start();

        
        latch.await();

        if (test.testResult == false) {
            throw new RuntimeException("User Clicked Fail! "
                    + "Wrong Unicode Character");
        }
    }
}

class X11KeysymTest implements Runnable {

    private static GridBagLayout layout;
    private static Panel mainControlPanel;
    private static Panel resultButtonPanel;
    private static TextArea instructionTextArea;
    private static Button passButton;
    private static Button failButton;
    private static Frame mainFrame;
    private static TextArea testArea;
    private final CountDownLatch latch;
    public volatile boolean testResult = false;

    public X11KeysymTest(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        createUI();
    }

    public final void createUI() {

        mainFrame = new Frame("X11 Keysym Test");
        layout = new GridBagLayout();
        mainControlPanel = new Panel(layout);
        resultButtonPanel = new Panel(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        String instructions
                = "INSTRUCTIONS:"
                + "\n Have a custom X11 keyboard layout with"
                + " \"topt\" assigned to some key:  "
                + "\n Map \"topt\" key to \"Caps_Lock\" by executing"
                + " following command in Terminal:"
                + "\n xmodmap -e \"keysym Caps_Lock = topt\"."
                + "\n Go to TextArea below and press \"CAPSLOCK\" key"
                + "\n If Symbol: " + "\u252c" + " is displayed then test Pass,"
                + "\n If Symbol: " + "\u242c" + " is displayed then test Fail,"
                + "\n Execute the below command to reset the above settigs, "
                + "\n setxkbmap -layout us";

        instructionTextArea = new TextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEnabled(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(instructionTextArea, gbc);

        testArea = new TextArea("TextArea");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainControlPanel.add(testArea, gbc);
        passButton = new Button("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            testResult = true;
            mainFrame.dispose();
            latch.countDown();

        });
        failButton = new Button("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testResult = false;
                mainFrame.dispose();
                latch.countDown();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        resultButtonPanel.add(passButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        resultButtonPanel.add(failButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainControlPanel.add(resultButtonPanel, gbc);

        mainFrame.add(mainControlPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

}

