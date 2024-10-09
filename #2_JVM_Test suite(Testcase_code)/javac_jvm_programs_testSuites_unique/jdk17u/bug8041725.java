import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.nimbus.*;

public class bug8041725 {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("bug8041725");
                frame.setSize(200, 200);
                JList list = new JList(new String[] { "Item1", "Item2", "Item3" });
                frame.getContentPane().add(list);
                frame.pack();
                frame.setVisible(true);
                System.err.println("Test #1: No items are selected, list is enabled.");
                testSelectionColors(list);
                System.err.println("Test #2: No items are selected, list is disabled.");
                list.setEnabled(false);
                testSelectionColors(list);
                System.err.println("Test #3: One item is selected, list is disabled.");
                list.setSelectedIndex(0);
                testSelectionColors(list);
                System.err.println("Test #4: One item is selected, list is enabled.");
                list.setEnabled(true);
                testSelectionColors(list);
                frame.dispose();
            }
        });
    }

    private static void testSelectionColors(JList list) {
        Color selBackColor = list.getSelectionBackground();
        if (!(selBackColor instanceof UIResource)) {
            throw new RuntimeException(String.format("JList.getSelectionBackground() returned instance of '%s' instead of UIResource.", selBackColor.getClass()));
        }
        Color selForeColor = list.getSelectionForeground();
        if (!(selForeColor instanceof UIResource)) {
            throw new RuntimeException(String.format("JList.getSelectionForeground() returned instance of '%s' instead of UIResource.", selForeColor.getClass()));
        }
    }
}
