import java.io.File;
import java.io.FileReader;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class bug8078268 {

    static volatile boolean parsingDone = false;

    static volatile Exception exception;

    public static void main(String[] args) throws Exception {
        long s = System.currentTimeMillis();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                Document doc = htmlKit.createDefaultDocument();
                try {
                    htmlKit.read(new FileReader(getDirURL() + "slowparse.html"), doc, 0);
                    parsingDone = true;
                } catch (Exception e) {
                    exception = e;
                }
            }
        });
        while (!parsingDone && exception == null && System.currentTimeMillis() - s < 5_000) {
            Thread.sleep(200);
        }
        final long took = System.currentTimeMillis() - s;
        if (exception != null) {
            throw exception;
        }
        if (took > 5_000) {
            throw new RuntimeException("Parsing takes too long.");
        }
    }

    static String getDirURL() {
        return new File(System.getProperty("test.src", ".")).getAbsolutePath() + File.separator;
    }
}
