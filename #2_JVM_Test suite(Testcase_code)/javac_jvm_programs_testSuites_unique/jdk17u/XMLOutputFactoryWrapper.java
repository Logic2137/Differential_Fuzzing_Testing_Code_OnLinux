
package xwp;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

public class XMLOutputFactoryWrapper extends XMLOutputFactory {

    private XMLOutputFactory defaultImpl = XMLOutputFactory.newDefaultFactory();

    @Override
    public XMLStreamWriter createXMLStreamWriter(Writer stream) throws XMLStreamException {
        return defaultImpl.createXMLStreamWriter(stream);
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLStreamWriter(stream);
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream stream, String encoding) throws XMLStreamException {
        return defaultImpl.createXMLStreamWriter(stream, encoding);
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        return defaultImpl.createXMLStreamWriter(result);
    }

    @Override
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        return defaultImpl.createXMLEventWriter(result);
    }

    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLEventWriter(stream);
    }

    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream stream, String encoding) throws XMLStreamException {
        return defaultImpl.createXMLEventWriter(stream, encoding);
    }

    @Override
    public XMLEventWriter createXMLEventWriter(Writer stream) throws XMLStreamException {
        return defaultImpl.createXMLEventWriter(stream);
    }

    @Override
    public void setProperty(String name, Object value) throws IllegalArgumentException {
        defaultImpl.setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return defaultImpl.getProperty(name);
    }

    @Override
    public boolean isPropertySupported(String name) {
        return defaultImpl.isPropertySupported(name);
    }
}
