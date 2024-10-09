import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class bug4492274 {

    private static URL page;

    private static JEditorPane jep;

    private static JFrame f;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    try {
                        page = new URL(page, "#linkname");
                        jep.setPage(page);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            robot.waitForIdle();
            if (getPageAnchor() == null) {
                throw new RuntimeException("JEditorPane.getPage() returns null anchor reference");
            }
        } finally {
            if (f != null)
                SwingUtilities.invokeAndWait(() -> f.dispose());
        }
    }

    private static String getPageAnchor() throws Exception {
        final String[] result = new String[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                result[0] = jep.getPage().getRef();
            }
        });
        return result[0];
    }

    private static void createAndShowGUI() {
        try {
            File file = new File(System.getProperty("test.src", "."), "test.html");
            page = file.toURI().toURL();
            f = new JFrame();
            jep = new JEditorPane();
            jep.setEditorKit(new HTMLEditorKit());
            jep.setEditable(false);
            jep.setPage(page);
            JScrollPane sp = new JScrollPane(jep);
            f.getContentPane().add(sp);
            f.setSize(500, 500);
            f.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
