

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class LostTextTest {

    static DefaultTableModel model;

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        LostText test = new LostText(latch);
        Thread T1 = new Thread(test);
        T1.start();

        
        boolean ret = false;
        try {
            ret = latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            throw ie;
        }
        if (!ret) {
            test.dispose();
            throw new RuntimeException(" User has not executed the test");
        }

        if (test.testResult == false) {
            throw new RuntimeException("Some text were not rendered properly"
                    + " during painting of Jtable rows ");
        }
    }
}

class LostText implements Runnable {
    static JFrame f;
    static JDialog dialog;
    static DefaultTableModel model;
    public boolean testResult = false;
    private final CountDownLatch latch;

    public LostText(CountDownLatch latch) throws Exception {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            createUI();
            lostTextTest();
        } catch (Exception ex) {
            if (f != null) {
                f.dispose();
            }
            latch.countDown();
            throw new RuntimeException("createUI Failed: " + ex.getMessage());
        }

    }

    public void dispose() {
        dialog.dispose();
        f.dispose();
    }

    private static void lostTextTest() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                f = new JFrame();
                f.add(getComp());
                f.setSize(300, 300);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }

            private Component getComp() {
                JTable jTable = new JTable(testSelectionWithFilterTable());
                return jTable;
            }
        });
    }

    private static TableModel testSelectionWithFilterTable() {
        model = new DefaultTableModel(0, 1);
        int last = 10;
        for (int i = 0; i <= last; i++) {
            model.addRow(new Object[]{i});
        }
        return model;
    }

    private final void createUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                String description
                        = " INSTRUCTIONS:\n"
                        + " A JTable will be shown.\n"
                        + " Try to select different rows via mouse or keyboard.\n "
                        + " Please verify if text are painted properly.\n"
                        + " If any moment any part of the rows will not be\n "
                        + " painted properly and if some text are missing in JTable,\n "
                        + " then press fail else press pass";

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
