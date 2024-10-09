

package xp2;

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

public class XMLEventFactoryImpl extends XMLEventFactory {

    @Override
    public void setLocation(Location location) {

    }

    @Override
    public Attribute createAttribute(String prefix, String namespaceURI, String localName, String value) {
        return null;
    }

    @Override
    public Attribute createAttribute(String localName, String value) {
        return null;
    }

    @Override
    public Attribute createAttribute(QName name, String value) {
        return null;
    }

    @Override
    public Namespace createNamespace(String namespaceURI) {
        return null;
    }

    @Override
    public Namespace createNamespace(String prefix, String namespaceUri) {
        return null;
    }

    @Override
    public StartElement createStartElement(QName name, Iterator attributes, Iterator namespaces) {
        return null;
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName) {
        return null;
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName,
            Iterator attributes, Iterator namespaces) {
        return null;
    }

    @Override
    public StartElement createStartElement(String prefix, String namespaceUri, String localName,
            Iterator attributes, Iterator namespaces, NamespaceContext context) {
        return null;
    }

    @Override
    public EndElement createEndElement(QName name, Iterator namespaces) {
        return null;
    }

    @Override
    public EndElement createEndElement(String prefix, String namespaceUri, String localName) {
        return null;
    }

    @Override
    public EndElement createEndElement(String prefix, String namespaceUri, String localName,
            Iterator namespaces) {
        return null;
    }

    @Override
    public Characters createCharacters(String content) {
        return null;
    }

    @Override
    public Characters createCData(String content) {
        return null;
    }

    @Override
    public Characters createSpace(String content) {
        return null;
    }

    @Override
    public Characters createIgnorableSpace(String content) {
        return null;
    }

    @Override
    public StartDocument createStartDocument() {
        return null;
    }

    @Override
    public StartDocument createStartDocument(String encoding, String version, boolean standalone) {
        return null;
    }

    @Override
    public StartDocument createStartDocument(String encoding, String version) {
        return null;
    }

    @Override
    public StartDocument createStartDocument(String encoding) {
        return null;
    }

    @Override
    public EndDocument createEndDocument() {
        return null;
    }

    @Override
    public EntityReference createEntityReference(String name, EntityDeclaration declaration) {
        return null;
    }

    @Override
    public Comment createComment(String text) {
        return null;
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return null;
    }

    @Override
    public DTD createDTD(String dtd) {
        return null;
    }

}
