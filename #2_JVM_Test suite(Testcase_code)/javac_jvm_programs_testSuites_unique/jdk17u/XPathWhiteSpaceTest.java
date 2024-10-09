import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class XPathWhiteSpaceTest {

    static final String XSDFILE = "idJ029.xsd";

    public static void main(String[] args) throws Exception {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(System.getProperty("test.src", "."), XSDFILE));
        } catch (SAXException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
