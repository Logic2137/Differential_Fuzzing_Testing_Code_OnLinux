import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.CountDownLatch;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;

public class ExternalDriveNameTest {

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        TestUI test = new TestUI(latch);
        SwingUtilities.invokeAndWait(() -> {
            try {
                test.createUI();
            } catch (Exception ex) {
                throw new RuntimeException("Exception while creating UI");
            }
        });
        boolean status = latch.await(5, TimeUnit.MINUTES);
        if (!status) {
            System.out.println("Test timed out.");
        }
        SwingUtilities.invokeAndWait(() -> {
            try {
                test.disposeUI();
            } catch (Exception ex) {
                throw new RuntimeException("Exception while disposing UI");
            }
        });
        if (test.testResult == false) {
            throw new RuntimeException("Test Failed.");
        }
    }
}

class TestUI {

    private static JFrame mainFrame;

    private static JPanel mainControlPanel;

    private static JTextArea instructionTextArea;

    private static JPanel resultButtonPanel;

    private static JButton passButton;

    private static JButton failButton;

    private static GridBagLayout layout;

    private final CountDownLatch latch;

    public boolean testResult = false;

    public TestUI(CountDownLatch latch) throws Exception {
        this.latch = latch;
    }

    public final void createUI() throws Exception {
        mainFrame = new JFrame("JFileChooser_ExternalDriveNameTest");
        layout = new GridBagLayout();
        mainControlPanel = new JPanel(layout);
        resultButtonPanel = new JPanel(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        String instructions = "INSTRUCTIONS:" + "\n 1. This is a Windows 10 specific test. If you are not on " + "Windows 10, press Pass." + "\n 2. Make sure you have an External Drive attached to your " + "computer." + "\n 3. Open a JFileChooser by clicking on launch button." + "\n 4. In JFileChooser dropdown, there are two Desktop " + "locations." + "\n 5. One Desktop is child of My PC and one is parent of it." + "\n 6. Open the parent Desktop folder." + "\n 7. You should see the External Drive in the list of " + "files." + "\n 8. If the External drive name is empty (it does not have " + "any name), press Fail, else press Pass.";
        instructionTextArea = new JTextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEnabled(false);
        instructionTextArea.setDisabledTextColor(Color.black);
        instructionTextArea.setBackground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(instructionTextArea, gbc);
        JButton launchButton = new JButton("Launch");
        launchButton.setActionCommand("Launch");
        launchButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(null);
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainControlPanel.add(launchButton, gbc);
        passButton = new JButton("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            testResult = true;
            mainFrame.dispose();
            latch.countDown();
        });
        failButton = new JButton("Fail");
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

    public void disposeUI() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }
}
