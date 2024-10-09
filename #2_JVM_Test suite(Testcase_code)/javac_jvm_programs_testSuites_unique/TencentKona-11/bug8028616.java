



import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;

public class bug8028616 {
    private static final String text = "/ at start is bad";
    private static Object lock = new Object();
    private static boolean isCallbackInvoked = false;
    private static Exception exception = null;

    public static void main(String[] args) throws Exception {
        ParserCB cb = new ParserCB();
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();

        htmlDoc.getParser().parse(new StringReader(text), cb, true);

        synchronized (lock) {
            if (!isCallbackInvoked) {
                lock.wait(5000);
            }
        }

        if (!isCallbackInvoked) {
            throw new RuntimeException("Test Failed: ParserCallback.handleText() is not invoked for text - " + text);
        }

        if (exception != null) {
            throw exception;
        }
    }

    private static class ParserCB extends HTMLEditorKit.ParserCallback {
        @Override
        public void handleText(char[] data, int pos) {
            synchronized (lock) {
                if (!text.equals(new String(data)) || pos != 0) {
                    exception = new RuntimeException(
                        "Test Failed: the data passed to ParserCallback.handleText() does not meet the expectation");
                }
                isCallbackInvoked = true;
                lock.notifyAll();
            }
        }
    }
}

