import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPasswordField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.GapContent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.StringContent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

public final class CleanInternalStorageOnSetText {

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            testStorage(false, new JPasswordField());
            testStorage(true, new JPasswordField());
            Document document = new PlainDocument(new StringContent());
            testStorage(false, new JPasswordField(document, "", 10));
            document = new PlainDocument(new StringContent());
            testStorage(true, new JPasswordField(document, "", 10));
            document = new PlainDocument(new GapContent());
            testStorage(false, new JPasswordField(document, "", 10));
            document = new PlainDocument(new GapContent());
            testStorage(true, new JPasswordField(document, "", 10));
            document = new HTMLDocument(new StringContent(), new StyleSheet());
            testStorage(false, new JPasswordField(document, "", 10));
            document = new HTMLDocument(new StringContent(), new StyleSheet());
            testStorage(true, new JPasswordField(document, "", 10));
            document = new HTMLDocument(new GapContent(), new StyleSheet());
            testStorage(false, new JPasswordField(document, "", 10));
            document = new HTMLDocument(new GapContent(), new StyleSheet());
            testStorage(true, new JPasswordField(document, "", 10));
        });
    }

    private static void testStorage(boolean makeGap, JPasswordField pf) {
        test(pf, "123", makeGap);
        test(pf, "1234567", makeGap);
        test(pf, "1234567890", makeGap);
        test(pf, "1".repeat(100), makeGap);
        test(pf, "1234567890", makeGap);
        test(pf, "1234567", makeGap);
        test(pf, "123", makeGap);
        test(pf, "", makeGap);
    }

    private static void test(JPasswordField pf, String text, boolean makeGap) {
        pf.setText(text);
        if (makeGap && text.length() > 3) {
            try {
                pf.getDocument().remove(1, 2);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
        char[] internalArray = getInternalArray(pf);
        ArrayList<Segment> segments = new ArrayList<>();
        if (makeGap) {
            Document doc = pf.getDocument();
            int nleft = doc.getLength();
            Segment sgm = new Segment();
            sgm.setPartialReturn(true);
            int offs = 0;
            try {
                while (nleft > 0) {
                    doc.getText(offs, nleft, sgm);
                    segments.add(sgm);
                    nleft -= sgm.count;
                    offs += sgm.count;
                }
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
        System.err.println("Before = " + Arrays.toString(internalArray));
        pf.setText("");
        System.err.println("After = " + Arrays.toString(internalArray));
        if (!makeGap) {
            for (char c : internalArray) {
                if (c != '\u0000' && c != '\n') {
                    throw new RuntimeException(Arrays.toString(internalArray));
                }
            }
        } else {
            for (Segment sgm : segments) {
                for (int i = sgm.offset; i < sgm.count + sgm.offset; i++) {
                    char c = sgm.array[i];
                    if (c != '\u0000' && c != '\n') {
                        throw new RuntimeException(Arrays.toString(sgm.array));
                    }
                }
            }
        }
    }

    private static char[] getInternalArray(JPasswordField pf) {
        Document doc = pf.getDocument();
        int nleft = doc.getLength();
        Segment text = new Segment();
        int offs = 0;
        text.setPartialReturn(true);
        try {
            doc.getText(offs, nleft, text);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        return text.array;
    }
}
