import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Insets;

public class TestDisabledToolTipBorder {

    private static TestUI test;

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        test = new TestUI(latch);
        SwingUtilities.invokeAndWait(() -> {
            try {
                test.createUI();
            } catch (Exception ex) {
                throw new RuntimeException("Exception while creating UI");
            }
        });
        boolean status = latch.await(2, TimeUnit.MINUTES);
        if (!status) {
            System.out.println("Test timed out.");
        }
        if (test.testResult == false) {
            disposeUI();
            throw new RuntimeException("Test Failed.");
        }
    }

    public static void disposeUI() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                if (test != null) {
                    test.disposeUI();
                }
            } catch (Exception ex) {
                throw new RuntimeException("Exception while disposing UI");
            }
        });
    }
}

class TestUI {

    private static JFrame mainFrame;

    private static JPanel mainControlPanel;

    private static JTextArea instructionTextArea;

    private static JPanel resultButtonPanel;

    private static JButton passButton;

    private static JButton failButton;

    private GridBagConstraints gbc;

    private static GridBagLayout layout;

    private final CountDownLatch latch;

    public boolean testResult = false;

    public final void initialize() throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }

    public TestUI(CountDownLatch latch) throws Exception {
        this.latch = latch;
        initialize();
    }

    public final void createUI() throws Exception {
        mainFrame = new JFrame();
        layout = new GridBagLayout();
        mainControlPanel = new JPanel(layout);
        resultButtonPanel = new JPanel(layout);
        gbc = new GridBagConstraints();
        String instructions = "Move cursor over disabled button.\n" + "Check if there is a border around the tooltip text \n" + "If yes, click on 'pass' else click on 'fail'\n";
        instructionTextArea = new JTextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEditable(false);
        instructionTextArea.setBorder(BorderFactory.createTitledBorder("Test Instructions"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainControlPanel.add(instructionTextArea, gbc);
        customize();
        passButton = new JButton("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            System.out.println("Pass Button pressed!");
            testResult = true;
            latch.countDown();
            disposeUI();
        });
        failButton = new JButton("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener((ActionEvent e) -> {
            System.out.println("Fail Button pressed!");
            testResult = false;
            latch.countDown();
            disposeUI();
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
        mainFrame.dispose();
    }

    private void customize() throws Exception {
        JPanel customButtonPanel = new JPanel(layout);
        mainFrame.setTitle("TestDisabledToolTipBorder");
        JButton disabledButton = new JButton("Disabled Button");
        disabledButton.setToolTipText("TooltipText");
        disabledButton.setMargin(new Insets(30, 30, 30, 30));
        disabledButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        customButtonPanel.add(disabledButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainControlPanel.add(customButtonPanel, gbc);
    }
}
