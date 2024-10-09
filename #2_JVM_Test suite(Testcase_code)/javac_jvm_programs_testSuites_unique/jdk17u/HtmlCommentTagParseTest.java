import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class HtmlCommentTagParseTest {

    private static volatile boolean failed = false;

    public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            MyParser cb = new MyParser();
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
            FileReader reader = null;
            try {
                reader = new FileReader(getDirURL() + "test.html");
                htmlDoc.getParser().parse(reader, cb, true);
                if (failed) {
                    throw new RuntimeException("Test failed");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String getDirURL() {
        return new File(System.getProperty("test.src", ".")).getAbsolutePath() + File.separator;
    }

    private static class MyParser extends HTMLEditorKit.ParserCallback {

        @Override
        public void handleError(String errorMsg, int pos) {
            failed = errorMsg.contains("eof.script");
        }
    }
}
