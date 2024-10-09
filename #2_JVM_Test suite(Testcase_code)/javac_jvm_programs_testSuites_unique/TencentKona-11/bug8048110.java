



import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class bug8048110 {
    private static Robot robot;
    private static Object lock = new Object();
    private static boolean isRealSyncPerformed = false;
    private static final String htmlText = "<table width=\"100%\" cellpadding=\"10\" cellspacing=\"5\" align=\"center\">" +
            "<tr><th align=\"left\" bgcolor=\"#bec3c6\">Devices</th><th align=\"left\" bgcolor=\"#bec3c6\">State</th></tr>" +
            "<tr><td align=\"left\" bgcolor=\"#bec3c6\">PC</td><td align=\"left\" bgcolor=\"#46a055\">Ok</td></tr></table>";

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                robot.waitForIdle();
                synchronized (lock) {
                    isRealSyncPerformed = true;
                    lock.notifyAll();
                }
            }
        };
        thread.start();

        synchronized (lock) {
            if (!isRealSyncPerformed) {
                lock.wait(5000);
            }
        }

        if (!isRealSyncPerformed) {
            throw new RuntimeException("Test Failed!");
        }
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        HTMLEditorKit editorKit = new HTMLEditorKit();
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditorKit(editorKit);
        textPane.setText("Initial text without table");

        JFrame frame = new JFrame("bug8048110");
        frame.getContentPane().add(textPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 200);
        frame.setVisible(true);

        textPane.setDocument(textPane.getEditorKit().createDefaultDocument());
        HTMLDocument htmlDocument = (HTMLDocument) textPane.getDocument();
        Element firstParagraph = findFirstElement(textPane.getDocument().getDefaultRootElement(), "p");

        try {
            htmlDocument.setInnerHTML(firstParagraph, htmlText);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Element findFirstElement(Element e, String name) {
        String elementName = e.getName();
        if (elementName != null && elementName.equalsIgnoreCase(name)) {
            return e;
        }
        for (int i = 0; i < e.getElementCount(); i++) {
            Element result = findFirstElement(e.getElement(i), name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}

