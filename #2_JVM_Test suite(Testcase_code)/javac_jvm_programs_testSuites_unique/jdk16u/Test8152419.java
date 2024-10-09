

 

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Test8152419 {

    public static void main(String args[]) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        JColorChooserTest test = new JColorChooserTest(latch);
        Thread T1 = new Thread(test);
        T1.start();

        
        try {
            latch.await();
        } catch (InterruptedException ie) {
            throw ie;
        }

        if (test.testResult == false) {
            throw new RuntimeException("User Clicked Fail!");
        }
    }
}

class JColorChooserTest implements Runnable {

    private static GridBagLayout layout;
    private static JPanel mainControlPanel;
    private static JPanel resultButtonPanel;
    private static JTextArea instructionTextArea;
    private static JButton passButton;
    private static JButton failButton;
    private static JFrame mainFrame;
    private static JColorChooser colorChooser;
    private final CountDownLatch latch;
    public volatile boolean testResult = false;

    public JColorChooserTest(CountDownLatch latch) throws Exception {
        this.latch = latch;
    }

    @Override
    public void run() {

        try {
            createUI();
        } catch (Exception ex) {
            if (mainFrame != null) {
                mainFrame.dispose();
            }
            latch.countDown();
            throw new RuntimeException("createUI Failed: " + ex.getMessage());
        }

    }

    public final void createUI() throws Exception {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                mainFrame = new JFrame("JColorChooser Test");
                layout = new GridBagLayout();
                mainControlPanel = new JPanel(layout);
                resultButtonPanel = new JPanel(layout);

                GridBagConstraints gbc = new GridBagConstraints();
                String instructions
                        = "INSTRUCTIONS:"
                        + "\n Select HSV, HSL, RGB or CMYK tab."
                        + "\n If able to shift and view JColorChooser tabs"
                        + "\n then click Pass, else Fail.";

                instructionTextArea = new JTextArea();
                instructionTextArea.setText(instructions);
                instructionTextArea.setEnabled(true);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                mainControlPanel.add(instructionTextArea, gbc);

                UIManager.put("FormattedTextField.border",
                        new EmptyBorder(0, 10, 0, 10));
                colorChooser = new JColorChooser(Color.BLUE);
                gbc.gridx = 0;
                gbc.gridy = 1;
                mainControlPanel.add(colorChooser, gbc);

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
        });

    }
}

