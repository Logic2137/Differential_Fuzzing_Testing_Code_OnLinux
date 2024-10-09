
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;


public class TextSelectionTest {

    private static final String INSTRUCTIONS = "This is a manual test.\n"
            + "\n"
            + "Select the current text from the end to the beginning.\n"
            + "\n"
            + "If the text is slightly shiftted from one side to another\n"
            + "and back during selection press Fail.\n"
            + "Otherwise, press Pass.";

    private static final CountDownLatch latch = new CountDownLatch(1);
    private static volatile boolean passed = false;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TextSelectionTest::createAndShowGUI);
        latch.await(3, TimeUnit.MINUTES);
        System.out.println("passed: " + passed);
        if (!passed) {
            throw new RuntimeException("Test fails!");
        }
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Follow the instructions below:");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JTextComponent textComponent = new JTextArea(INSTRUCTIONS);
        textComponent.setEditable(false);
        Font font = textComponent.getFont();
        font = font.deriveFont(24.0f);
        textComponent.setFont(font);
        panel.add(textComponent, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton passButton = new JButton("Pass");
        passButton.addActionListener((e) -> {
            passed = true;
            latch.countDown();
            frame.dispose();
        });
        JButton failsButton = new JButton("Fail");
        failsButton.addActionListener((e) -> {
            passed = false;
            latch.countDown();
            frame.dispose();
        });

        buttonsPanel.add(passButton);
        buttonsPanel.add(failsButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                latch.countDown();
            }
        });
        frame.setVisible(true);
    }
}
