


import java.awt.Dimension;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlFontSizeTest {
    static volatile Dimension w3cFrameSize;
    static volatile Dimension stdFrameSize;

    private static Dimension test(boolean w3ccheck) {
        JEditorPane htmlPane = new JEditorPane();
        htmlPane.setEditable(false);

        if (w3ccheck) {
            htmlPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
        }

        HTMLEditorKit kit = new HTMLEditorKit();
        htmlPane.setEditorKit(kit);

        String htmlString = "<html>\n"
            + "<body style=\"font-family:SansSerif; font-size:16pt\">\n"
            + "<p>This should be 16 pt.</p>\n"
            + "</body>\n"
            + "</html>";

        Document doc = kit.createDefaultDocument();
        htmlPane.setDocument(doc);
        htmlPane.setText(htmlString);

        return htmlPane.getPreferredSize();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            w3cFrameSize = test(true);
            stdFrameSize = test(false);
        });
        System.out.println("size with W3C:" + w3cFrameSize);
        System.out.println("size without W3C:" + stdFrameSize);

        float ratio = (float)w3cFrameSize.width / (float)stdFrameSize.width;
        System.out.println("w3cFrameSize.width/stdFrameSize.width " + ratio);

        if (!"1.3".equals(String.format(Locale.ENGLISH, "%.1f", ratio))) {
            throw new RuntimeException("HTML font size too large with high-DPI scaling and W3C_LENGTH_UNITS");
        }
    }
}
