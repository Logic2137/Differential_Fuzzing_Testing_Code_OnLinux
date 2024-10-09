




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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Dimension;

public class bug4310381 {
    private static TestUI test = null;
    private static JFrame frame = null;
    private static JTabbedPane tab = null;
    private static JPanel panel = null;
    private static CountDownLatch testLatch = null;

    private static void init() {
        frame = new JFrame();
        tab = new JTabbedPane();
        panel = new JPanel();
        createContentPane();
        tab.setTabPlacement(JTabbedPane.TOP);
        tab.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        frame.setMinimumSize(new Dimension(100, 200));
    }

    public static void main(String[] args) throws Exception {
        testLatch = new CountDownLatch(1);
        test = new TestUI(testLatch);

        
        SwingUtilities.invokeAndWait(() -> {
            try {
                test.createUI();
            } catch (Exception ex) {
                throw new RuntimeException("Exception while creating UI");
            }
        });

        
        for(UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            
            setLookAndFeel(laf);

            
            SwingUtilities.invokeAndWait(() -> {
                init();
                frame.setTitle(laf.getClassName());
                showUI();
            });

            final CountDownLatch latch = new CountDownLatch(1);
            if(!latch.await(10, TimeUnit.SECONDS)) {
                frame.setVisible(false);
            }

            
            SwingUtilities.invokeAndWait(() -> {
                frame.dispose();
            });
        }

        boolean status = testLatch.await(1, TimeUnit.MINUTES);
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
            if(test != null) {
                test.disposeUI();
            }
        });
    }

    static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) throws Exception {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            disposeUI();
            throw new RuntimeException(e);
        }
    }

    public static void showUI() {
        frame.getContentPane().add(tab);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(100, 200));
        frame.pack();
        frame.setVisible(true);
    }

    private static void createContentPane() {
        String a2z = "abcdefghijklmnopqrstuvwxyz";

        tab.addTab("0" + a2z + a2z, new JLabel((UIManager.getLookAndFeel())
                        .getName() + " Look and Feel"));
        tab.addTab("1" + a2z, new JLabel("1" + a2z));
        tab.addTab("2" + a2z, new JLabel("2" + a2z));
        tab.addTab("3", new JPanel()); 
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

    public TestUI(CountDownLatch latch) {
        this.latch = latch;
    }

    public final void createUI() throws Exception {
        mainFrame = new JFrame();

        layout = new GridBagLayout();
        mainControlPanel = new JPanel(layout);
        resultButtonPanel = new JPanel(layout);

        gbc = new GridBagConstraints();

        
        String instructions
            = "See for different look and feel tabbed panes titles \n"
            + "   contain three dots(...) at the end if the pane size \n"
            + "   cannot accommodate the title completely.\n"
            + "If yes, click on 'pass' else click on 'fail'\n";

        instructionTextArea = new JTextArea();
        instructionTextArea.setText(instructions);
        instructionTextArea.setEditable(false);
        instructionTextArea.setBorder(BorderFactory.
                createTitledBorder("Test Instructions"));

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
        
        mainFrame.setTitle("Tabbed Pane Title Test");
   }
}
