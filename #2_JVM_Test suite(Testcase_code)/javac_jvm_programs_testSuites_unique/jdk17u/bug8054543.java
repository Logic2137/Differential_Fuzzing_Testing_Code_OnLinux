import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class bug8054543 {

    public bug8054543() {
        JLayer<JComponent> layer = new JLayer<>();
        Border border = BorderFactory.createLineBorder(Color.GREEN);
        JButton view = new JButton("JButton");
        layer.setBorder(border);
        check(layer.getBorder(), null);
        layer.setBorder(null);
        check(layer.getBorder(), null);
        layer.setView(view);
        check(layer.getBorder(), view.getBorder());
        layer.setBorder(border);
        check(border, view.getBorder());
        layer.setBorder(null);
        check(layer.getBorder(), view.getBorder());
    }

    private void check(Object o1, Object o2) {
        if (o1 != o2) {
            throw new RuntimeException("Test failed");
        }
    }

    public static void main(String... args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                new bug8054543();
            }
        });
    }
}
