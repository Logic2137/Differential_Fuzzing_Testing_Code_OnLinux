

package transform;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

public class VersionEventWriter implements XMLEventWriter {

    private String version = null;

    private String encoding = null;

    
    public VersionEventWriter() {
    }

    public void add(XMLEvent event) throws XMLStreamException {
        if (event.getEventType() == XMLStreamConstants.START_DOCUMENT) {
            version = ((StartDocument) event).getVersion();
            encoding = ((StartDocument) event).getCharacterEncodingScheme();
        }
    }

    public void flush() throws XMLStreamException {
    }

    public void close() throws XMLStreamException {
    }

    public void add(XMLEventReader reader) throws XMLStreamException {
    }

    public java.lang.String getPrefix(java.lang.String uri) throws XMLStreamException {
        return null;
    }

    public void setPrefix(java.lang.String prefix, java.lang.String uri) throws XMLStreamException {
    }

    public void setDefaultNamespace(java.lang.String uri) throws XMLStreamException {
    }

    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
    }

    public NamespaceContext getNamespaceContext() {
        return null;
    }

    public String getVersion() {
        return version;
    }

    public String getEncoding() {
        return encoding;
    }
}
