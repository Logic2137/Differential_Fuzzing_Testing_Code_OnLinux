
package transform;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class VersionDefaultHandler extends DefaultHandler {

    private String version = null;

    private String encoding = null;

    public VersionDefaultHandler() {
    }

    Locator saxLocator = null;

    public void setDocumentLocator(Locator locator) {
        saxLocator = locator;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        version = ((org.xml.sax.ext.Locator2) saxLocator).getXMLVersion();
        encoding = ((org.xml.sax.ext.Locator2) saxLocator).getEncoding();
    }

    public void error(SAXParseException e) throws SAXException {
        e.printStackTrace();
    }

    public String getVersion() {
        return version;
    }

    public String getEncoding() {
        return encoding;
    }
}
