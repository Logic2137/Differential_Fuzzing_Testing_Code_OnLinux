
package xwp;

import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.transform.Source;

public class XMLInputFactoryWrapper extends XMLInputFactory {

    private XMLInputFactory defaultImpl = XMLInputFactory.newDefaultFactory();

    @Override
    public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(reader);
    }

    @Override
    public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(source);
    }

    @Override
    public XMLStreamReader createXMLStreamReader(InputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(stream);
    }

    @Override
    public XMLStreamReader createXMLStreamReader(InputStream stream, String encoding) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(stream, encoding);
    }

    @Override
    public XMLStreamReader createXMLStreamReader(String systemId, InputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(systemId, stream);
    }

    @Override
    public XMLStreamReader createXMLStreamReader(String systemId, Reader reader) throws XMLStreamException {
        return defaultImpl.createXMLStreamReader(systemId, reader);
    }

    @Override
    public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(reader);
    }

    @Override
    public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(systemId, reader);
    }

    @Override
    public XMLEventReader createXMLEventReader(XMLStreamReader reader) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(reader);
    }

    @Override
    public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(source);
    }

    @Override
    public XMLEventReader createXMLEventReader(InputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(stream);
    }

    @Override
    public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(stream, encoding);
    }

    @Override
    public XMLEventReader createXMLEventReader(String systemId, InputStream stream) throws XMLStreamException {
        return defaultImpl.createXMLEventReader(systemId, stream);
    }

    @Override
    public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter) throws XMLStreamException {
        return defaultImpl.createFilteredReader(reader, filter);
    }

    @Override
    public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter) throws XMLStreamException {
        return defaultImpl.createFilteredReader(reader, filter);
    }

    @Override
    public XMLResolver getXMLResolver() {
        return defaultImpl.getXMLResolver();
    }

    @Override
    public void setXMLResolver(XMLResolver resolver) {
        defaultImpl.setXMLResolver(resolver);
    }

    @Override
    public XMLReporter getXMLReporter() {
        return defaultImpl.getXMLReporter();
    }

    @Override
    public void setXMLReporter(XMLReporter reporter) {
        defaultImpl.setXMLReporter(reporter);
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

    @Override
    public void setEventAllocator(XMLEventAllocator allocator) {
        defaultImpl.setEventAllocator(allocator);
    }

    @Override
    public XMLEventAllocator getEventAllocator() {
        return defaultImpl.getEventAllocator();
    }
}
