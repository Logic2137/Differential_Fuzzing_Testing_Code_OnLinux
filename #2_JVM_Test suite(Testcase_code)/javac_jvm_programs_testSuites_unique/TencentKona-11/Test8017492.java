import java.util.Vector;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Element;
import sun.awt.SunToolkit;

public class Test8017492 {

    public static void main(String[] args) throws Exception {
        Runnable task = new Runnable() {

            @Override
            public void run() {
                try {
                    SunToolkit.createNewAppContext();
                    DTD dtd = DTD.getDTD("dtd");
                    dtd.elements = new Vector<Element>() {

                        @Override
                        public synchronized int size() {
                            return Integer.MAX_VALUE;
                        }
                    };
                    dtd.getElement("element");
                } catch (Exception exception) {
                    throw new Error("unexpected", exception);
                }
            }
        };
        Thread thread = new Thread(new ThreadGroup("$$$"), task);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                System.exit(1);
            }
        });
        thread.start();
        thread.join();
        SunToolkit.createNewAppContext();
        HTMLDocument document = new HTMLDocument() {

            @Override
            public HTMLEditorKit.ParserCallback getReader(int pos) {
                return getReader(pos, 0, 0, null);
            }

            @Override
            public HTMLEditorKit.ParserCallback getReader(int pos, int popDepth, int pushDepth, HTML.Tag insertTag) {
                return new HTMLDocument.HTMLReader(pos, popDepth, pushDepth, insertTag) {

                    @Override
                    public void handleError(String error, int pos) {
                        throw new Error(error);
                    }
                };
            }
        };
        new HTMLEditorKit().insertHTML(document, 0, "<html><body>text", 0, 0, null);
    }
}
