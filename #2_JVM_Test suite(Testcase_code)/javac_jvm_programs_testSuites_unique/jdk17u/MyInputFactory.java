
package stream.XMLInputFactoryTest;

import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.transform.Source;

public class MyInputFactory extends javax.xml.stream.XMLInputFactory {

    @Override
    public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createXMLStreamReader(InputStream stream) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createXMLStreamReader(InputStream stream, String encoding) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createXMLStreamReader(String systemId, InputStream stream) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createXMLStreamReader(String systemId, Reader reader) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(XMLStreamReader reader) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(InputStream stream) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createXMLEventReader(String systemId, InputStream stream) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLResolver getXMLResolver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setXMLResolver(XMLResolver resolver) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLReporter getXMLReporter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setXMLReporter(XMLReporter reporter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProperty(String name, Object value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPropertySupported(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEventAllocator(XMLEventAllocator allocator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XMLEventAllocator getEventAllocator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
