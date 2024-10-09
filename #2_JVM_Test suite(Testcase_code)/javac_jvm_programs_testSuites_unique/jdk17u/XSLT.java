import java.io.ByteArrayOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLT {

    static final String XMLTOTRANSFORM = "/in.xml";

    static final String XSLTRANSFORMER = "/test.xsl";

    static final String EXPECTEDRESULT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>ABCDEFG:null";

    public static void main(String[] args) throws TransformerException {
        ByteArrayOutputStream resStream = new ByteArrayOutputStream();
        TransformerFactory trf = TransformerFactory.newInstance();
        Transformer tr = trf.newTransformer(new StreamSource(System.getProperty("test.src", ".") + XSLTRANSFORMER));
        tr.transform(new StreamSource(System.getProperty("test.src", ".") + XMLTOTRANSFORM), new StreamResult(resStream));
        System.out.println("Transformation completed. Result:" + resStream.toString());
        if (!resStream.toString().equals(EXPECTEDRESULT)) {
            throw new RuntimeException("Incorrect transformation result");
        }
    }
}
