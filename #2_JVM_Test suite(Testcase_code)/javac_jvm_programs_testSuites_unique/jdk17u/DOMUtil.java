
package stream.XMLStreamWriterTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMUtil {

    private static DocumentBuilder db;

    private static String fixNull(String s) {
        if (s == null)
            return "";
        else
            return s;
    }

    public static Document createDom() {
        synchronized (DOMUtil.class) {
            if (db == null) {
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    throw new FactoryConfigurationError(e);
                }
            }
            return db.newDocument();
        }
    }

    public static Node createDOMNode(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            try {
                return builder.parse(inputStream);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException pce) {
            IllegalArgumentException iae = new IllegalArgumentException(pce.getMessage());
            iae.initCause(pce);
            throw iae;
        }
        return null;
    }

    public static void serializeNode(Element node, XMLStreamWriter writer) throws XMLStreamException {
        String nodePrefix = fixNull(node.getPrefix());
        String nodeNS = fixNull(node.getNamespaceURI());
        boolean prefixDecl = isPrefixDeclared(writer, nodeNS, nodePrefix);
        writer.writeStartElement(nodePrefix, node.getLocalName(), nodeNS);
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            int numOfAttributes = attrs.getLength();
            for (int i = 0; i < numOfAttributes; i++) {
                Node attr = attrs.item(i);
                String nsUri = fixNull(attr.getNamespaceURI());
                if (nsUri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                    String local = attr.getLocalName().equals(XMLConstants.XMLNS_ATTRIBUTE) ? "" : attr.getLocalName();
                    if (local.equals(nodePrefix) && attr.getNodeValue().equals(nodeNS)) {
                        prefixDecl = true;
                    }
                    writer.setPrefix(attr.getLocalName(), attr.getNodeValue());
                    writer.writeNamespace(attr.getLocalName(), attr.getNodeValue());
                }
            }
        }
        if (!prefixDecl) {
            writer.writeNamespace(nodePrefix, nodeNS);
        }
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            int numOfAttributes = attrs.getLength();
            for (int i = 0; i < numOfAttributes; i++) {
                Node attr = attrs.item(i);
                String attrPrefix = fixNull(attr.getPrefix());
                String attrNS = fixNull(attr.getNamespaceURI());
                if (!attrNS.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                    String localName = attr.getLocalName();
                    if (localName == null) {
                        localName = attr.getNodeName();
                    }
                    boolean attrPrefixDecl = isPrefixDeclared(writer, attrNS, attrPrefix);
                    if (!attrPrefix.equals("") && !attrPrefixDecl) {
                        writer.setPrefix(attr.getLocalName(), attr.getNodeValue());
                        writer.writeNamespace(attrPrefix, attrNS);
                    }
                    writer.writeAttribute(attrPrefix, attrNS, localName, attr.getNodeValue());
                }
            }
        }
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                switch(child.getNodeType()) {
                    case Node.PROCESSING_INSTRUCTION_NODE:
                        writer.writeProcessingInstruction(child.getNodeValue());
                    case Node.DOCUMENT_TYPE_NODE:
                        break;
                    case Node.CDATA_SECTION_NODE:
                        writer.writeCData(child.getNodeValue());
                        break;
                    case Node.COMMENT_NODE:
                        writer.writeComment(child.getNodeValue());
                        break;
                    case Node.TEXT_NODE:
                        writer.writeCharacters(child.getNodeValue());
                        break;
                    case Node.ELEMENT_NODE:
                        serializeNode((Element) child, writer);
                        break;
                }
            }
        }
        writer.writeEndElement();
    }

    private static boolean isPrefixDeclared(XMLStreamWriter writer, String nsUri, String prefix) {
        boolean prefixDecl = false;
        NamespaceContext nscontext = writer.getNamespaceContext();
        Iterator prefixItr = nscontext.getPrefixes(nsUri);
        while (prefixItr.hasNext()) {
            if (prefix.equals(prefixItr.next())) {
                prefixDecl = true;
                break;
            }
        }
        return prefixDecl;
    }

    public static Element getFirstChild(Element e, String nsUri, String local) {
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element c = (Element) n;
                if (c.getLocalName().equals(local) && c.getNamespaceURI().equals(nsUri))
                    return c;
            }
        }
        return null;
    }
}
