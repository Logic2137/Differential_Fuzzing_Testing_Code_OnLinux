import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.JSlider;

public class SliderTickTest {

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        TestUI test = new TestUI(latch);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    test.createUI();
                } catch (Exception ex) {
                    throw new RuntimeException("Exception while creating UI");
                }
            }
        });
        boolean status = latch.await(5, TimeUnit.MINUTES);
        if (!status) {
            System.out.println("Test timed out.");
        }
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    test.disposeUI();
                } catch (Exception ex) {
                    throw new RuntimeException("Exception while disposing UI");
                }
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
        mainFrame = new JFrame("SliderTickTest");
        layout = new GridBagLayout();
        mainControlPanel = new JPanel(layout);
        resultButtonPanel = new JPanel(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        String instructions = "INSTRUCTIONS:" + "\n Click PageUp/PageDown key. If the slider indicator" + "\n moves to Next/Previous immediate minor tick, then " + "\n test passes else failed.";
        instructionTextArea = new JTextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEnabled(false);
        instructionTextArea.setDisabledTextColor(Color.black);
        instructionTextArea.setBackground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(instructionTextArea, gbc);
        JSlider slider = new JSlider(0, 50);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(30);
        slider.setBorder(BorderFactory.createTitledBorder("Ticks"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainControlPanel.add(slider, gbc);
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
