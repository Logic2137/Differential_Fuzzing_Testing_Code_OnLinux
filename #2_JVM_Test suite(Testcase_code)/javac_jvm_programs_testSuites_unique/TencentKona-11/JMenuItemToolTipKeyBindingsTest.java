



import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class JMenuItemToolTipKeyBindingsTest {
    private static final long TIMEOUT = 5 * 60 * 1000;
    private static final AtomicBoolean testCompleted = new AtomicBoolean(false);
    private static volatile boolean testResult = false;

    private static Dialog controlDialog;
    private static JFrame testFrame;

    private static final String instructions =
            "Verify that \"CTRL\" + \"F1\" key sequence shows/hides tool tip message" +
            "\nfor menu items.\n" +
            "\n1. Open pop-up menu \"Menu\", (i.e. press \"F10\")." +
            "\n2. Navigate to some menu element using keyboard." +
            "\n3. Press \"CTRL\" + \"F1\" once menu item is selected." +
            "\nIf tooltip message is displayed for the item then press \"Pass\"," +
            "\n otherwise press \"Fail\".";

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> createAndShowGUI());

            waitForCompleting();
            if (!testResult) {
                throw new RuntimeException("Test FAILED!");
            }
        } finally {
            if (controlDialog != null) {
                controlDialog.dispose();
            }
            if (testFrame != null) {
                testFrame.dispose();
            }
        }
    }

    private static void createAndShowGUI() {
        controlDialog = new Dialog((JFrame)null, "JMenuItemToolTipKeyBindingsTest");

        TextArea messageArea = new TextArea(instructions, 15, 80, TextArea.SCROLLBARS_BOTH);
        controlDialog.add("North", messageArea);

        Button passedButton = new Button("Pass");
        passedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testResult = true;
                completeTest();
            }
        });

        Button failedButton = new Button("Fail");
        failedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testResult = false;
                completeTest();
            }
        });

        Panel buttonPanel = new Panel();
        buttonPanel.add("West",passedButton);
        buttonPanel.add("East", failedButton);
        controlDialog.add("South", buttonPanel);

        controlDialog.setBounds(250, 0, 500, 500);
        controlDialog.setVisible(true);

        testFrame = new JFrame("JMenuItemToolTipKeyBindingsTest");
        testFrame.setSize(200, 200);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Menu");
        for (int i = 0; i < 3; i++) {
            JMenuItem jMenuItem = new JMenuItem("Item " + i);
            jMenuItem.setToolTipText("Tooltip " + i);
            jMenu.add(jMenuItem);
        }
        jMenuBar.add(jMenu);
        testFrame.setJMenuBar(jMenuBar);
        testFrame.setVisible(true);
    }

    private static void completeTest() {
        testCompleted.set(true);
        synchronized (testCompleted) {
            testCompleted.notifyAll();
        }
    }

    private static void waitForCompleting() throws Exception {
        synchronized (testCompleted) {
            long startTime = System.currentTimeMillis();
            while (!testCompleted.get()) {
                testCompleted.wait(TIMEOUT);
                if (System.currentTimeMillis() - startTime >= TIMEOUT) {
                    break;
                }
            }
        }
    }
}

