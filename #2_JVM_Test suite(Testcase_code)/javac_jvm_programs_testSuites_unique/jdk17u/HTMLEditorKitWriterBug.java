import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HTMLEditorKitWriterBug {

    public static void main(String[] args) {
        String htmlDoc = "<pre><p> </pre>";
        try {
            HTMLEditorKit kit = new HTMLEditorKit();
            Class c = Class.forName("javax.swing.text.html.parser.ParserDelegator");
            HTMLEditorKit.Parser parser = (HTMLEditorKit.Parser) c.newInstance();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            HTMLEditorKit.ParserCallback htmlReader = doc.getReader(0);
            parser.parse(new CharArrayReader(htmlDoc.toCharArray()), htmlReader, true);
            htmlReader.flush();
            CharArrayWriter writer = new CharArrayWriter(1000);
            kit.write(writer, doc, 0, doc.getLength());
            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException("Test Failed " + ex);
        }
    }
}
