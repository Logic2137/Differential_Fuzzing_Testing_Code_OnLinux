import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;

public class TestBrowserBGColor extends JFrame implements HyperlinkListener {

    private static TestBrowserBGColor b;

    private static JEditorPane browser;

    public static void main(final String[] args) throws Exception {
        Robot r = new Robot();
        SwingUtilities.invokeAndWait(() -> {
            try {
                b = new TestBrowserBGColor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            b.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            b.setVisible(true);
            b.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            b.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    b.dispose();
                    b = null;
                }
            });
        });
        r.waitForIdle();
        r.delay(500);
        SwingUtilities.invokeAndWait(() -> {
            Insets insets = browser.getInsets();
            Point loc = browser.getLocationOnScreen();
            Color c = r.getPixelColor(loc.x + insets.left + 100, loc.y + insets.top + 100);
            b.dispose();
            if (!c.equals(Color.WHITE)) {
                throw new RuntimeException("webpage background color wrong");
            }
        });
    }

    String htmlDoc = " <!DOCTYPE html> <html><style> body { background: #FFF; } </style> <head> <title>Title</title> </head> <body> </body> </html>";

    public TestBrowserBGColor() throws IOException, MalformedURLException {
        browser = new JEditorPane("text/html", htmlDoc);
        browser.setEditable(false);
        browser.addHyperlinkListener(this);
        JScrollPane scroll = new JScrollPane(browser);
        getContentPane().add(scroll);
    }

    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                HTMLDocument doc = (HTMLDocument) pane.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
            } else {
                try {
                    pane.setPage(e.getURL());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
