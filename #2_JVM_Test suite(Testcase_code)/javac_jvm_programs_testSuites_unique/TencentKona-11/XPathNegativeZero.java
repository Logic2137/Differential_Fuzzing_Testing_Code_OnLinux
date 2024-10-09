



import java.io.File;
import java.io.StringWriter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;


public class XPathNegativeZero {

    static final String EXPECTEDXML = "<newtop>\"0\"</newtop>";

    public static void main(final String[] args) throws Exception {
        
        final String xml = System.getProperty("test.src", ".")+"/dummy.xml";
        
        final String xsl = System.getProperty("test.src", ".")+"/negativezero.xsl";
        final String result = xform(xml, xsl).trim();

        System.out.println("transformed XML: '"+result+ "' expected XML: '"+EXPECTEDXML+"'");
        if (!result.equals(EXPECTEDXML))
            throw new Exception("Negative zero was incorrectly transformed");
    }

    private static String xform(final String xml, final String xsl) throws Exception {
        final TransformerFactory tf = TransformerFactory.newInstance();
        final Source xslsrc = new StreamSource(new File(xsl));
        final Templates tmpl = tf.newTemplates(xslsrc);
        final Transformer t = tmpl.newTransformer();

        StringWriter writer = new StringWriter();
        final Source src = new StreamSource(new File(xml));
        final Result res = new StreamResult(writer);

        t.transform(src, res);
        return writer.toString();
    }
}
