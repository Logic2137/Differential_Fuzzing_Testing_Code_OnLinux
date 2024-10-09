
package javax.xml.validation.ptests;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

class MyErrorHandler implements ErrorHandler {

    public void error(SAXParseException exception) throws SAXParseException {
        throw exception;
    }

    public void warning(SAXParseException exception) throws SAXParseException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXParseException {
        throw exception;
    }
}
