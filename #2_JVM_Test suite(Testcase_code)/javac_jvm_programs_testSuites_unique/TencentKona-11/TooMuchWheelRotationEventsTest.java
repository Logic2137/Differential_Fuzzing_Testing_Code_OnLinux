

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class TooMuchWheelRotationEventsTest {

    private static volatile boolean testResult = false;
    private static volatile CountDownLatch countDownLatch;
    private static final String INSTRUCTIONS = " INSTRUCTIONS:\n"
            + " Try to check the issue with trackpad\n"
            + "\n"
            + " If the trackpad is not supported, press PASS\n"
            + "\n"
            + " Use the trackpad to slightly scroll the JTextArea horizontally and vertically.\n"
            + " If the text area is scrolled too fast press FAIL, else press PASS.";

    public static void main(String args[]) throws Exception {
        countDownLatch = new CountDownLatch(1);

        SwingUtilities.invokeLater(TooMuchWheelRotationEventsTest::createUI);
        countDownLatch.await(15, TimeUnit.MINUTES);

        if (!testResult) {
            throw new RuntimeException("Test fails!");
        }
    }

    private static void createUI() {

        final JFrame mainFrame = new JFrame("Trackpad scrolling test");
        GridBagLayout layout = new GridBagLayout();
        JPanel mainControlPanel = new JPanel(layout);
        JPanel resultButtonPanel = new JPanel(layout);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel testPanel = createTestPanel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(testPanel, gbc);

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
            mainFrame.dispose();
            countDownLatch.countDown();

        });

        JButton failButton = new JButton("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                countDownLatch.countDown();
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

        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                countDownLatch.countDown();
            }
        });
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private static JPanel createTestPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextArea textArea = new JTextArea(20, 20);
        textArea.setText(getLongString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        return panel;
    }

    private static String getLongString() {

        String lowCaseString = getLongString('a', 'z');
        String upperCaseString = getLongString('A', 'Z');
        String digitsString = getLongString('0', '9');

        int repeat = 30;
        StringBuilder lowCaseBuilder = new StringBuilder();
        StringBuilder upperCaseBuilder = new StringBuilder();
        StringBuilder digitsBuilder = new StringBuilder();

        for (int i = 0; i < repeat; i++) {
            lowCaseBuilder.append(lowCaseString).append(' ');
            upperCaseBuilder.append(upperCaseString).append(' ');
            digitsBuilder.append(digitsString).append(' ');
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            builder.append(upperCaseBuilder).append('\n')
                    .append(lowCaseBuilder).append('\n')
                    .append(digitsBuilder).append("\n\n\n");
        }

        return builder.toString();
    }

    private static String getLongString(char c1, char c2) {

        char[] chars = new char[c2 - c1 + 1];
        for (char i = c1; i <= c2; i++) {
            chars[i - c1] = i;
        }
        return new String(chars);
    }
}
