



import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.ScrollPaneConstants;

public class ThemeReaderCrashTest {

    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ThemeReaderCrashTest obj = new ThemeReaderCrashTest();
            }
        });
    }

    ThemeReaderCrashTest() {
        JPanel panel = new JPanel();
        JScrollPane pane =
            new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.setSize(300, 200);

        panel.add(pane);
    }
}

