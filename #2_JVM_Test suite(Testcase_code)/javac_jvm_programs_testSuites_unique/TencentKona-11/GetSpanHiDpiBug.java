

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GetSpanHiDpiBug {
    public static void main(String[] args) throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        SpanTest test = new SpanTest(latch);
        Thread T1 = new Thread(test);
        T1.start();

        
        boolean ret = false;
        try {
            ret = latch.await(3000, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            throw ie;
        }
        if (!ret) {
            test.dispose();
            throw new RuntimeException(" User has not executed the test");
        }
        if (test.testResult == false) {
            throw new RuntimeException("Some characters overlap");
        }
    }
}

class SpanTest implements Runnable {
    static JFrame f;
    static JDialog dialog;
    public boolean testResult = false;
    private final CountDownLatch latch;

    public SpanTest(CountDownLatch latch) throws Exception {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            createUI();
            spanTest();
        } catch (Exception ex) {
            dispose();
            latch.countDown();
            throw new RuntimeException("createUI Failed: " + ex.getMessage());
        }
    }

    public void dispose() {
        if (dialog != null) {
            dialog.dispose();
        }
        if (f != null) {
            f.dispose();
        }
    }

    private static void spanTest() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JLabel label =
                    new JLabel("<html><span>A few words to get started "
                    + "before the bug</span><span>overlapping text</span></html>");
                f = new JFrame("");
                f.getContentPane().add(label, BorderLayout.CENTER);
                f.setSize(500,500);
                f.setVisible(true);
            }
        });
    }

    private final void createUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                String description
                        = " INSTRUCTIONS:\n"
                        + " A string will be shown.\n "
                        + " Press Pass if there is no overlap of characters\n"
                        + " else press Fail.";

                dialog = new JDialog();
                dialog.setTitle("textselectionTest");
                JTextArea textArea = new JTextArea(description);
                textArea.setEditable(false);
                final JButton passButton = new JButton("PASS");
                passButton.addActionListener((e) -> {
                    testResult = true;
                    dispose();
                    latch.countDown();
                });
                final JButton failButton = new JButton("FAIL");
                failButton.addActionListener((e) -> {
                    testResult = false;
                    dispose();
                    latch.countDown();
                });
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.add(textArea, BorderLayout.CENTER);
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(passButton);
                buttonPanel.add(failButton);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                dialog.add(mainPanel);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
}
