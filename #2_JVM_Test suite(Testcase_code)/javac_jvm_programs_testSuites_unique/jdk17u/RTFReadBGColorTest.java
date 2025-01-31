import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.rtf.RTFEditorKit;

public class RTFReadBGColorTest {

    static JTextPane text;

    static String BGTEXT = "yellow_background\n";

    public static void main(String[] a) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JFrame f = new JFrame();
            f.setBounds(200, 600, 400, 300);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            text = new JTextPane();
            text.setEditorKit(new RTFEditorKit());
            MutableAttributeSet attrBackground = new SimpleAttributeSet();
            StyleConstants.setBackground(attrBackground, Color.YELLOW);
            try {
                text.getDocument().insertString(0, BGTEXT, attrBackground);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            write();
            read();
            f.getContentPane().add(text);
            f.setVisible(true);
            text.setCaretPosition(BGTEXT.length() + 6);
            StyledDocument style = text.getStyledDocument();
            AttributeSet oldSet = style.getCharacterElement(BGTEXT.length() + 6).getAttributes();
            f.dispose();
            if (!style.getBackground(oldSet).equals(Color.YELLOW)) {
                throw new RuntimeException("RTFEditorKit does not read background color");
            }
        });
    }

    static void write() {
        try (OutputStream o = Files.newOutputStream(Paths.get("test.rtf"))) {
            text.getEditorKit().write(o, text.getDocument(), 0, 0);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    static void read() {
        try (InputStream in = Files.newInputStream(Paths.get("test.rtf"))) {
            text.getEditorKit().read(in, text.getDocument(), 0);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }
}
