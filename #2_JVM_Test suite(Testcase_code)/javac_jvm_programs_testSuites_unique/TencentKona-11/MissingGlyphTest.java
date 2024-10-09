

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class MissingGlyphTest {
    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Mac")) {
            return;
        }
        SwingUtilities.invokeAndWait(() -> {
            doTest(MissingGlyphTest::glyphTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(180000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Alef character is not rendered");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void glyphTest() {
        frame = new JFrame("Test");
        frame.add(new MyComponent());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static synchronized void pass() {
        testPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        testPassed = false;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    private static void doTest(Runnable action) {
        String description
                = " The test is supposed to display arabic alef character.\n"
                + " If it resembles like the one shown in\n "
                + " www.fileformat.info/info/unicode/char/0627/index.htm\n "
                + " in Italic style ,press PASS.\n"
                + " If the glyph is not shown or empty rectangle is shown, press FAIL";

        final JDialog dialog = new JDialog();
        dialog.setTitle("printBannerTest");
        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        final JButton testButton = new JButton("Start Test");
        final JButton passButton = new JButton("PASS");
        passButton.setEnabled(false);
        passButton.addActionListener((e) -> {
            dialog.dispose();
            frame.dispose();
            pass();
        });
        final JButton failButton = new JButton("FAIL");
        failButton.setEnabled(false);
        failButton.addActionListener((e) -> {
            dialog.dispose();
            frame.dispose();
            fail();
        });
        testButton.addActionListener((e) -> {
            testButton.setEnabled(false);
            action.run();
            passButton.setEnabled(true);
            failButton.setEnabled(true);
        });
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(testButton);
        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
           @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("main dialog closing");
                testGeneratedInterrupt = false;
                mainThread.interrupt();
            }
        });
    }
}

class MyComponent extends JComponent {
    private final Font font = new Font("Menlo", Font.ITALIC, 100);
    private final String text = "\u0627"; 

    @Override
    protected void paintComponent(Graphics g) {
        if (font.canDisplayUpTo(text) == -1) {
            g.setColor(Color.black);
            g.setFont(font);
            g.drawString(text, 70, 110);
        }
    }
}




