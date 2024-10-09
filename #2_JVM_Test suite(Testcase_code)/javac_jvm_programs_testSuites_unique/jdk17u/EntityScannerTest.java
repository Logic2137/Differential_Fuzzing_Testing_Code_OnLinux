import java.io.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class EntityScannerTest {

    public static void main(String[] args) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("<root attr=\"");
            for (int i = 0; i < 200; i++) {
                builder.append("\n");
            }
            builder.append("foo.");
            builder.append("\" />");
            final XMLReader reader = XMLReaderFactory.createXMLReader();
            System.out.println(reader.getClass().getName());
            reader.parse(new InputSource(new StringReader(builder.toString())));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Test failed: ArrayIndexOutOfBoundsException " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
