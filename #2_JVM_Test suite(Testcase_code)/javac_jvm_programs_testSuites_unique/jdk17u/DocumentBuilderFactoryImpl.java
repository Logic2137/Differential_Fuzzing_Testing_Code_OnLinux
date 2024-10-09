
package xp1;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
    }

    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        return false;
    }
}
