import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.io.*;

public class RTFWriteParagraphAlignTest {

    public static final String[] RTF_DATA = { "{\\rtf1\\ansi\\ansicpg1252\\" + "cocoartf949\\cocoasubrtf350" + "{\\fonttbl\\f0\\fswiss\\fcharset0 Helvetica;}" + "{\\colortbl;\\red255\\green255\\blue255;}" + "\\margl1440\\margr1440\\vieww9000\\viewh8400\\viewkind0" + "\\pard\\ql\\pardirnatural" + "\\f0\\fs24 \\cf0 left}", "{\\rtf1\\ansi\\ansicpg1252\\" + "cocoartf949\\cocoasubrtf350" + "{\\fonttbl\\f0\\fswiss\\fcharset0 Helvetica;}" + "{\\colortbl;\\red255\\green255\\blue255;}" + "\\margl1440\\margr1440\\vieww9000\\viewh8400\\viewkind0" + "\\pard\\qc\\pardirnatural" + "\\f0\\fs24 \\cf0 center}", "{\\rtf1\\ansi\\ansicpg1252\\" + "cocoartf949\\cocoasubrtf350" + "{\\fonttbl\\f0\\fswiss\\fcharset0 Helvetica;}" + "{\\colortbl;\\red255\\green255\\blue255;}" + "\\margl1440\\margr1440\\vieww9000\\viewh8400\\viewkind0" + "\\pard\\qr\\pardirnatural" + "\\f0\\fs24 \\cf0 right}", "{\\rtf1\\ansi\\ansicpg1252\\" + "cocoartf949\\cocoasubrtf350" + "{\\fonttbl\\f0\\fswiss\\fcharset0 Helvetica;}" + "{\\colortbl;\\red255\\green255\\blue255;}" + "\\margl1440\\margr1440\\vieww9000\\viewh8400\\viewkind0" + "\\pard\\qj\\pardirnatural" + "\\f0\\fs24 \\cf0 justifiedtextjustifiedtext " + "justifiedtextjustifiedtext justifiedtextjustifiedtext " + "justifiedtextjustifiedtext justifiedtextjustifiedtext }" };

    private static JFrame frame;

    private static JTextPane jTextPane;

    private static int position1;

    private static int position2;

    private static RTFEditorKit rtfEditorKit;

    private static Robot robot;

    public static void main(String[] args) throws Exception {
        rtfEditorKit = new RTFEditorKit();
        robot = new Robot();
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(600, 200);
            jTextPane = new JTextPane();
            frame.getContentPane().add(jTextPane);
            frame.setVisible(true);
        });
        test(StyleConstants.ALIGN_LEFT);
        test(StyleConstants.ALIGN_CENTER);
        test(StyleConstants.ALIGN_RIGHT);
        test(StyleConstants.ALIGN_JUSTIFIED);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
        System.out.println("ok");
    }

    static void test(int align) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            jTextPane.setDocument(rtfEditorKit.createDefaultDocument());
            try {
                rtfEditorKit.read(new ByteArrayInputStream(RTF_DATA[align].getBytes()), jTextPane.getDocument(), 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            try {
                int endOffset = jTextPane.getDocument().getRootElements()[0].getElement(0).getEndOffset();
                position1 = jTextPane.modelToView(endOffset - 1).x;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        File tempFile = File.createTempFile("aaaa", ".rtf");
        tempFile.deleteOnExit();
        rtfEditorKit.write(new FileOutputStream(tempFile), jTextPane.getDocument(), 0, jTextPane.getDocument().getLength());
        Document d = rtfEditorKit.createDefaultDocument();
        rtfEditorKit.read(new FileInputStream(tempFile), d, 0);
        SwingUtilities.invokeAndWait(() -> jTextPane.setDocument(d));
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            try {
                int endOffset = jTextPane.getDocument().getRootElements()[0].getElement(0).getEndOffset();
                position2 = jTextPane.modelToView(endOffset - 1).x;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if (position1 != position2) {
            throw new RuntimeException("Alignment is not preserved after the " + "document write/read");
        }
    }
}
