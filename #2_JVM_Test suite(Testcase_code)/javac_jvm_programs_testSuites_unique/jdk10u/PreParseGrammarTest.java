



import com.sun.org.apache.xerces.internal.parsers.XMLGrammarPreparser;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PreParseGrammarTest {

    public static void main(String[] args) throws FileNotFoundException, XNIException, IOException {
        File xsdf = new File(System.getProperty("test.src", ".") + "/test.xsd");
        InputStream is = new BufferedInputStream(new FileInputStream(xsdf));
        XMLInputSource xis = new XMLInputSource(null, null, null, is, null);
        XMLGrammarPreparser gp = new XMLGrammarPreparser();
        gp.registerPreparser(XMLGrammarDescription.XML_SCHEMA, null);
        
        
        Grammar res = gp.preparseGrammar(XMLGrammarDescription.XML_SCHEMA, xis);
        System.out.println("Grammar preparsed successfully:" + res);
        return;
    }
}
