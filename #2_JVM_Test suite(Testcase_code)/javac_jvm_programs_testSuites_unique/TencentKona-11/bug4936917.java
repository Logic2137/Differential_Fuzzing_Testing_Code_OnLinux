




import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.util.Timer;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class bug4936917 {

    private boolean passed = false;
    private Timer timer;
    private JEditorPane editorPane;
    private static JFrame f;
    private volatile Point p = null;

    private String text =
                "<html><head><style>" +
                "body {background-color: #cccccc; margin-top: 36.000000pt;}" +
                "</style></head>" +
                "<body> some text </body></html>";

    public void init() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                editorPane = new JEditorPane("text/html", "");
                editorPane.setEditable(false);
                editorPane.setMargin(new java.awt.Insets(0, 0, 0, 0));
                editorPane.setText(text);

                f = new JFrame();
                f.getContentPane().add(editorPane);
                f.setSize(600, 400);
                f.setVisible(true);
            }
        });
        blockTillDisplayed(editorPane);
        Robot robot  = new Robot();
        robot.waitForIdle();
        robot.delay(300);

        int x0 = p.x + 15 ;
        int y = p.y + 15;
        int match = 0;
        int nonmatch = 0;

        passed = true;
        for (int x = x0; x < x0 + 10; x++) {
            System.out.println("color ("+x+"," + y +")=" + robot.getPixelColor(x,y));
            if (!robot.getPixelColor(x, y).equals(new Color(0xcc, 0xcc, 0xcc))) {
                nonmatch++;
            } else match++;
        }
        if (nonmatch > match) {
            passed = false;
        }
    }

    void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public void destroy() throws Exception {
        SwingUtilities.invokeAndWait(()->f.dispose());
        if(!passed) {
            throw new RuntimeException("Test failed.");
        }
    }


    public static void main(String args[]) throws Exception {
            bug4936917 test = new bug4936917();
            test.init();
            test.destroy();
    }
}
