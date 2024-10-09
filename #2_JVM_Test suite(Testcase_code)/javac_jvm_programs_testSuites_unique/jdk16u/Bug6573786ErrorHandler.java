

package parsers;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class Bug6573786ErrorHandler extends DefaultHandler {
    public boolean fail = false;

    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
        if (e.getMessage().indexOf("bad_value") < 0) {
            fail = true;
        }
    } 

    public void error(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
    } 

    public void warning(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
    } 
}
