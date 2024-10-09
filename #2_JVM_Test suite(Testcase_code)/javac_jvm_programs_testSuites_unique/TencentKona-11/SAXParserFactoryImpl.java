

package xp1;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class SAXParserFactoryImpl extends SAXParserFactory {

    @Override
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        return null;
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException,
            SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException, SAXNotRecognizedException,
            SAXNotSupportedException {
        return false;
    }

}
