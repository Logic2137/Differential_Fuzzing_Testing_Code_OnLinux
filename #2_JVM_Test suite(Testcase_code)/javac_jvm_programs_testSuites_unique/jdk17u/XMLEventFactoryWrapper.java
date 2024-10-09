
package xwp;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

public class XMLEventFactoryWrapper extends XMLEventFactory {

    private XMLEventFactory defaultImpl = XMLEventFactory.newDefaultFactory();

    @Override
    public void setLocation(Location location) {
        defaultImpl.setLocation(location);
    }

    @Override
    public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value) {
        return defaultImpl.createAttribute(prefix, namespaceURI, localName, value);
    }

    @Override
    public Attribute createAttribute(String localName, String value) {
        return defaultImpl.createAttribute(localName, value);
    }

    @Override
    public Attribute createAttribute(QName name, String value) {
        return defaultImpl.createAttribute(name, value);
    }

    @Override
    public Namespace createNamespace(String namespaceURI) {
        return defaultImpl.createNamespace(namespaceURI);
    }

    @Override
    public Namespace createNamespace(String prefix, String namespaceUri) {
        return defaultImpl.createNamespace(prefix, namespaceUri);
    }

    @Override
    public StartElement createStartElement(QName name, Iterator attributes, Iterator namespaces) {
        return defaultImpl.createStartElement(name, attributes, namespaces);
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
        return defaultImpl.createStartElement(prefix, namespaceUri, localName);
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces) {
        return defaultImpl.createStartElement(prefix, namespaceUri, localName, attributes, namespaces);
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName, Iterator attributes, Iterator namespaces, NamespaceContext context) {
        return defaultImpl.createStartElement(prefix, namespaceUri, localName, attributes, namespaces, context);
    }

    @Override
    public EndElement createEndElement(QName name, Iterator namespaces) {
        return defaultImpl.createEndElement(name, namespaces);
    }

    @Override
    public EndElement createEndElement(String prefix, String namespaceUri, String localName) {
        return defaultImpl.createEndElement(prefix, namespaceUri, localName);
    }

    @Override
    public EndElement createEndElement(String prefix, String namespaceUri, String localName, Iterator namespaces) {
        return defaultImpl.createEndElement(prefix, namespaceUri, localName, namespaces);
    }

    @Override
    public Characters createCharacters(String content) {
        return defaultImpl.createCharacters(content);
    }

    @Override
    public Characters createCData(String content) {
        return defaultImpl.createCData(content);
    }

    @Override
    public Characters createSpace(String content) {
        return defaultImpl.createSpace(content);
    }

    @Override
    public Characters createIgnorableSpace(String content) {
        return defaultImpl.createIgnorableSpace(content);
    }

    @Override
    public StartDocument createStartDocument() {
        return defaultImpl.createStartDocument();
    }

    @Override
    public StartDocument createStartDocument(String encoding, String version, boolean standalone) {
        return defaultImpl.createStartDocument(encoding, version, standalone);
    }

    @Override
    public StartDocument createStartDocument(String encoding, String version) {
        return defaultImpl.createStartDocument(encoding, version);
    }

    @Override
    public StartDocument createStartDocument(String encoding) {
        return defaultImpl.createStartDocument(encoding);
    }

    @Override
    public EndDocument createEndDocument() {
        return defaultImpl.createEndDocument();
    }

    @Override
    public EntityReference createEntityReference(String name, EntityDeclaration declaration) {
        return defaultImpl.createEntityReference(name, declaration);
    }

    @Override
    public Comment createComment(String text) {
        return defaultImpl.createComment(text);
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return defaultImpl.createProcessingInstruction(target, data);
    }

    @Override
    public DTD createDTD(String dtd) {
        return defaultImpl.createDTD(dtd);
    }
}
