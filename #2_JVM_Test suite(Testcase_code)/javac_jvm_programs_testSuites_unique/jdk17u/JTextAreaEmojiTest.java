import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JTextAreaEmojiTest implements ActionListener {

    private static GridBagLayout layout;

    private static JPanel textAreaPanel;

    private static JPanel mainControlPanel;

    private static JPanel instructionPanel;

    private static JPanel resultButtonPanel;

    private static JPanel controlPanel;

    private static JTextArea instructionTextArea;

    private static JTextArea emojiTextArea;

    private static JButton passButton;

    private static JButton failButton;

    private static JFrame mainFrame;

    private static final CountDownLatch testRunLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        JTextAreaEmojiTest test = new JTextAreaEmojiTest();
        boolean status = testRunLatch.await(5, TimeUnit.MINUTES);
        if (!status) {
            throw new RuntimeException("Test timed out");
        }
    }

    public JTextAreaEmojiTest() throws Exception {
        createControlPanelUI();
    }

    public final void createControlPanelUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                layout = new GridBagLayout();
                mainControlPanel = new JPanel(layout);
                instructionPanel = new JPanel(layout);
                resultButtonPanel = new JPanel(layout);
                textAreaPanel = new JPanel(layout);
                controlPanel = new JPanel(layout);
                GridBagConstraints gbc = new GridBagConstraints();
                String instructions = "1) Text Area size should be zero" + "\n2) Select one emoji from Character Viewer" + "\n3) If Text Area size increases displaying" + "Blank or supported emoji for default font, click pass" + "\n4) Else press fail";
                instructionTextArea = new JTextArea();
                instructionTextArea.setText(instructions);
                instructionTextArea.setEnabled(false);
                instructionTextArea.setDisabledTextColor(Color.black);
                instructionTextArea.setBackground(Color.white);
                instructionTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                instructionPanel.add(instructionTextArea, gbc);
                emojiTextArea = new JTextArea();
                emojiTextArea.setEnabled(true);
                emojiTextArea.setDisabledTextColor(Color.black);
                emojiTextArea.setBackground(Color.white);
                emojiTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                textAreaPanel.add(emojiTextArea, gbc);
                passButton = new JButton("Pass");
                passButton.setActionCommand("Pass");
                passButton.addActionListener(JTextAreaEmojiTest.this);
                failButton = new JButton("Fail");
                failButton.setActionCommand("Fail");
                failButton.addActionListener(JTextAreaEmojiTest.this);
                gbc.gridx = 0;
                gbc.gridy = 0;
                resultButtonPanel.add(passButton, gbc);
                gbc.gridx = 1;
                gbc.gridy = 0;
                resultButtonPanel.add(failButton, gbc);
                gbc.gridx = 0;
                gbc.gridy = 0;
                mainControlPanel.add(instructionPanel, gbc);
                gbc.gridx = 0;
                gbc.gridy = 1;
                mainControlPanel.add(textAreaPanel, gbc);
                gbc.gridx = 0;
                gbc.gridy = 2;
                mainControlPanel.add(resultButtonPanel, gbc);
                mainControlPanel.add(controlPanel, gbc);
                mainFrame = new JFrame("Control Panel");
                mainFrame.add(mainControlPanel);
                mainFrame.pack();
                mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton btn = (JButton) evt.getSource();
            switch(btn.getActionCommand()) {
                case "Pass":
                    break;
                case "Fail":
                    throw new AssertionError("Test case has failed!");
            }
            cleanUp();
        }
    }

    private static void cleanUp() {
        mainFrame.dispose();
        testRunLatch.countDown();
    }
}
