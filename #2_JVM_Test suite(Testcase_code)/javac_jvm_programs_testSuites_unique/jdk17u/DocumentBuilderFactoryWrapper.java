
package xwp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentBuilderFactoryWrapper extends DocumentBuilderFactory {

    private DocumentBuilderFactory defaultImpl = DocumentBuilderFactory.newDefaultInstance();

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        return defaultImpl.newDocumentBuilder();
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        defaultImpl.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        return defaultImpl.getAttribute(name);
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        defaultImpl.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        return defaultImpl.getFeature(name);
    }
}
