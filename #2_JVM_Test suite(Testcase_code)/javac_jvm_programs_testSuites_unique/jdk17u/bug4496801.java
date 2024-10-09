import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.*;
import javax.swing.text.*;
import java.io.IOException;

public class bug4496801 {

    public static void main(String[] args) {
        HTMLDocument doc = new HTMLDocument();
        doc.setParser(new ParserDelegator());
        Element html = doc.getRootElements()[0];
        Element body = html.getElement(0);
        try {
            doc.insertBeforeEnd(body, "<h2>foo</h2>");
        } catch (IOException e) {
        } catch (BadLocationException e) {
            throw new RuntimeException("Insertion failed");
        }
    }
}
