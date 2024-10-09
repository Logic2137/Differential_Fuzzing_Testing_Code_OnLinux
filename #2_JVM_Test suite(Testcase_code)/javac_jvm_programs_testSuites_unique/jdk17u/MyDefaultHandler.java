
package parsers;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.DefaultHandler;

public class MyDefaultHandler extends DefaultHandler {

    private Locator myLocator = null;

    String xmlVersion = "";

    public void setDocumentLocator(Locator locator) {
        myLocator = locator;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        try {
            xmlVersion = ((Locator2) myLocator).getXMLVersion();
        } catch (Exception e) {
        }
    }
}
