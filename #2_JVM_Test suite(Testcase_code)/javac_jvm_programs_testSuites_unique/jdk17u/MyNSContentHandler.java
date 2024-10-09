
package org.xml.sax.ptests;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import org.xml.sax.SAXException;

class MyNSContentHandler extends DefaultHandler implements AutoCloseable {

    private final static String WRITE_ERROR = "bWrite error";

    private final BufferedWriter bWriter;

    Locator locator = new LocatorImpl();

    public MyNSContentHandler(String outputFileName) throws SAXException {
        try {
            bWriter = new BufferedWriter(new FileWriter(outputFileName));
        } catch (IOException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        println("characters...length is:" + s.length() + "\n" + "<" + s + ">");
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            println("endDocument...");
            bWriter.flush();
            bWriter.close();
        } catch (IOException ex) {
            throw new SAXException(WRITE_ERROR, ex);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        println("endElement...\n" + "namespaceURI: <" + namespaceURI + "> localName: <" + localName + "> qName: <" + qName + ">");
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        println("endPrefixMapping...\n" + "prefix: <" + prefix + ">");
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        println("ignorableWhitespace...\n" + s + " ignorable white space string length: " + s.length());
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        println("processingInstruction...target:<" + target + "> data: <" + data + ">");
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        try {
            this.locator = locator;
            println("setDocumentLocator...");
        } catch (SAXException ex) {
            System.err.println(WRITE_ERROR + ex);
        }
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        println("skippedEntity...\n" + "name: <" + name + ">");
    }

    @Override
    public void startDocument() throws SAXException {
        println("startDocument...");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        println("startElement...\n" + "namespaceURI: <" + namespaceURI + "> localName: <" + localName + "> qName: <" + qName + "> Number of Attributes: <" + atts.getLength() + "> Line# <" + locator.getLineNumber() + ">");
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        println("startPrefixMapping...\n" + "prefix: <" + prefix + "> uri: <" + uri + ">");
    }

    private void println(String outString) throws SAXException {
        try {
            bWriter.write(outString, 0, outString.length());
            bWriter.newLine();
        } catch (IOException ex) {
            throw new SAXException(WRITE_ERROR, ex);
        }
    }

    @Override
    public void close() throws IOException {
        if (bWriter != null)
            bWriter.close();
    }
}
