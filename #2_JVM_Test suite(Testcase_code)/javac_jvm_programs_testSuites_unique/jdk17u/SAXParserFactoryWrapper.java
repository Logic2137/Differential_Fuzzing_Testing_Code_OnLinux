
package xwp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class SAXParserFactoryWrapper extends SAXParserFactory {

    private SAXParserFactory defaultImpl = SAXParserFactory.newDefaultInstance();

    @Override
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        return defaultImpl.newSAXParser();
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        defaultImpl.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        return defaultImpl.getFeature(name);
    }
}
