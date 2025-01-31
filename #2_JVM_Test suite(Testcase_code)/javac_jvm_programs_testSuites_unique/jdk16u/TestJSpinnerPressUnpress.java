



import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

public class TestJSpinnerPressUnpress {
    private static JFrame mainFrame = new JFrame();
    private static volatile boolean testResult = false;
    private static volatile CountDownLatch countDownLatch;
    private static final String GTK_LAF_CLASS = "GTKLookAndFeel";
    private static final String INSTRUCTIONS = "INSTRUCTIONS:\n\n" +
            "Verify that when spinner up/down button is pressed to change\n" +
            "the spinner value, the button's rendering is changed to show\n" +
            "the pressed state. If yes, Press Pass, Otherwise, Press Fail.";

    public static void main(String args[]) throws Exception {
        if (!System.getProperty("os.name").startsWith("Linux")) {
            System.out.println("This test is meant for Linux platform only");
            return;
        }
        countDownLatch = new CountDownLatch(1);

        for (UIManager.LookAndFeelInfo lookAndFeelInfo :
                UIManager.getInstalledLookAndFeels()) {
            if (lookAndFeelInfo.getClassName().contains(GTK_LAF_CLASS)) {
                try {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                } catch (final UnsupportedLookAndFeelException ignored) {
                    System.out.println("GTK L&F could not be set, so this " +
                            "test can not be run in this scenario ");
                    return;
                }
            }
        }
        SwingUtilities.invokeLater(TestJSpinnerPressUnpress::createUI);
        countDownLatch.await(15, TimeUnit.MINUTES);
        SwingUtilities.invokeLater(mainFrame::dispose);

        if (!testResult) {
            throw new RuntimeException("Test failed!");
        }
    }

    private static void createUI() {
        GridBagLayout layout = new GridBagLayout();
        JPanel mainControlPanel = new JPanel(layout);
        JPanel resultButtonPanel = new JPanel(layout);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(new JSpinner(), gbc);

        JTextArea instructionTextArea = new JTextArea();
        instructionTextArea.setText(INSTRUCTIONS);
        instructionTextArea.setEditable(false);
        instructionTextArea.setBackground(Color.white);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(instructionTextArea, gbc);

        JButton passButton = new JButton("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            testResult = true;
            countDownLatch.countDown();

        });

        JButton failButton = new JButton("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener(e -> {
            countDownLatch.countDown();
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

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                countDownLatch.countDown();
            }
        });
        mainFrame.setVisible(true);
    }
}
