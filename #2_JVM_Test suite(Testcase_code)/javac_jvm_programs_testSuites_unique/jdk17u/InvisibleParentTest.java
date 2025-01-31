import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class InvisibleParentTest {

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        TestUI test = new TestUI(latch);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    test.createUI();
                } catch (Exception ex) {
                    throw new RuntimeException("Exception while creating test UI");
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
                    throw new RuntimeException("Exception while disposing test UI");
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
        mainFrame = new JFrame("InvisibleParentTest");
        mainFrame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        layout = new GridBagLayout();
        mainControlPanel = new JPanel(layout);
        resultButtonPanel = new JPanel(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        String instructions = "When the test starts two windows should appear: frame G1 and\n" + "    dialog D1. Another one frame F1 should be minimized.\n" + "    If the dialog is not shown (minimizied), press FAIL button.\n" + "Then minimize frame G1 and restore F1. If the dialog D1 is not\n" + "    restored together with F1, press FAIL, else PASS";
        instructionTextArea = new JTextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEditable(false);
        instructionTextArea.setBorder(BorderFactory.createTitledBorder("Test Instructions"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainControlPanel.add(instructionTextArea, gbc);
        passButton = new JButton("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            System.out.println("Pass Button pressed!");
            testResult = true;
            latch.countDown();
        });
        failButton = new JButton("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Fail Button pressed!");
                testResult = false;
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
        gbc.gridy = 1;
        mainControlPanel.add(resultButtonPanel, gbc);
        mainFrame.add(mainControlPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
        createAWTComponents();
    }

    public void disposeUI() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }

    private void createAWTComponents() {
        Frame f1 = new Frame("F1");
        f1.setBounds(100, 300, 100, 100);
        f1.setVisible(true);
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }
        f1.setExtendedState(Frame.ICONIFIED);
        Frame g1 = new Frame("G1");
        g1.setBounds(150, 350, 100, 100);
        g1.setVisible(true);
        final Dialog d1 = new Dialog((Frame) null, "D1", Dialog.ModalityType.APPLICATION_MODAL);
        d1.setBounds(200, 400, 100, 100);
        new Thread(new Runnable() {

            public void run() {
                d1.setVisible(true);
            }
        }).start();
    }
}
