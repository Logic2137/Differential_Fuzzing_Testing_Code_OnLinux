



import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import java.io.StringReader;

public class Test6933784 {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new SynthLookAndFeel());

        checkImages();

        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        checkImages();
    }

    private static void checkImages() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                HTMLEditorKit c = new HTMLEditorKit();
                HTMLDocument doc = new HTMLDocument();

                try {
                    c.read(new StringReader("<HTML><TITLE>Test</TITLE><BODY><IMG id=test></BODY></HTML>"), doc, 0);
                } catch (Exception e) {
                    throw new RuntimeException("The test failed", e);
                }

                Element elem = doc.getElement("test");
                ImageView iv = new ImageView(elem);

                if (iv.getLoadingImageIcon() == null) {
                    throw new RuntimeException("getLoadingImageIcon returns null");
                }

                if (iv.getNoImageIcon() == null) {
                    throw new RuntimeException("getNoImageIcon returns null");
                }
            }
        });
    }
}
