



import javax.swing.*;
import java.awt.*;

public class ScrollFlickerTest {

    private static JFrame frame;
    private static JScrollPane scroll;
    private static int cnt = 0;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            frame.setSize(300, 200);
            frame.getContentPane().setLayout(null);
            JTextArea text = new JTextArea("Test test test test");
            text.setLineWrap(true);
            scroll = new JScrollPane(text);
            frame.getContentPane().add(scroll);
            scroll.setBounds(1, 1, 100, 50);
            frame.setVisible(true);
        });

        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(200);

        SwingUtilities.invokeAndWait(() -> {
            Insets insets = scroll.getInsets();
            scroll.setSize(insets.left + insets.right +
                    scroll.getVerticalScrollBar().getPreferredSize().width, 50);
            scroll.revalidate();
        });
        robot.delay(200);
        SwingUtilities.invokeAndWait(() ->
                          scroll.getViewport().addChangeListener((e) -> cnt++));
        robot.delay(1000);

        SwingUtilities.invokeLater(frame::dispose);

        if (cnt > 0) {
            throw new RuntimeException("Scroll bar flickers");
        }
    }
}
