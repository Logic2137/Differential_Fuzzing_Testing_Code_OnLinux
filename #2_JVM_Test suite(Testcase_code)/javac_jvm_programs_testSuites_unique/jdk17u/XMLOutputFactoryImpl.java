
package xp1;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

public class XMLOutputFactoryImpl extends XMLOutputFactory {

    @Override
    public XMLStreamWriter createXMLStreamWriter(Writer stream) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream stream) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(OutputStream stream, String encoding) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream stream) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLEventWriter createXMLEventWriter(OutputStream stream, String encoding) throws XMLStreamException {
        return null;
    }

    @Override
    public XMLEventWriter createXMLEventWriter(Writer stream) throws XMLStreamException {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) throws IllegalArgumentException {
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean isPropertySupported(String name) {
        return false;
    }
}
