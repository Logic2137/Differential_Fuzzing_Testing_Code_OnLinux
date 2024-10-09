
package test.auctionportal;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class XInclHandler extends DefaultHandler implements LexicalHandler {

    private final PrintWriter fOut;

    private volatile boolean fCanonical;

    private volatile int fElementDepth;

    public void setCanonical(boolean canonical) {
        fCanonical = canonical;
    }

    public XInclHandler(OutputStream stream, String encoding) throws UnsupportedEncodingException {
        if (encoding == null) {
            encoding = "UTF8";
        }
        fOut = new PrintWriter(new OutputStreamWriter(stream, encoding), false);
    }

    @Override
    public void startDocument() throws SAXException {
        fElementDepth = 0;
        if (!fCanonical) {
            writeFlush("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        if (fElementDepth > 0) {
            StringBuilder instruction = new StringBuilder("<?").append(target);
            if (data != null && data.length() > 0) {
                instruction.append(' ').append(data);
            }
            instruction.append("?>");
            writeFlush(instruction.toString());
        }
    }

    @Override
    public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {
        fElementDepth++;
        StringBuilder start = new StringBuilder().append('<').append(raw);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                start.append(' ').append(attrs.getQName(i)).append("=\"").append(normalizeAndPrint(attrs.getValue(i))).append('"');
            }
        }
        start.append('>');
        writeFlush(start.toString());
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        writeFlush(normalizeAndPrint(ch, start, length));
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String local, String raw) throws SAXException {
        fElementDepth--;
        writeFlush("</" + raw + ">");
    }

    @Override
    public void warning(SAXParseException ex) throws SAXException {
        printError("Warning", ex);
    }

    @Override
    public void error(SAXParseException ex) throws SAXException {
        printError("Error", ex);
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        printError("Fatal Error", ex);
        throw ex;
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override
    public void endDTD() throws SAXException {
    }

    @Override
    public void startEntity(String name) throws SAXException {
    }

    @Override
    public void endEntity(String name) throws SAXException {
    }

    @Override
    public void startCDATA() throws SAXException {
    }

    @Override
    public void endCDATA() throws SAXException {
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (!fCanonical && fElementDepth > 0) {
            writeFlush("<!--" + normalizeAndPrint(ch, start, length) + "-->");
        }
    }

    private String normalizeAndPrint(String s) {
        return s.chars().mapToObj(c -> normalizeAndPrint((char) c)).collect(Collectors.joining());
    }

    private String normalizeAndPrint(char[] ch, int offset, int length) {
        return normalizeAndPrint(new String(ch, offset, length));
    }

    private String normalizeAndPrint(char c) {
        switch(c) {
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '&':
                return "&amp;";
            case '"':
                return "&quot;";
            case '\r':
            case '\n':
                return fCanonical ? "&#" + Integer.toString(c) + ";" : String.valueOf(c);
            default:
                return String.valueOf(c);
        }
    }

    private void printError(String type, SAXParseException ex) {
        System.err.print("[" + type + "] ");
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1)
                systemId = systemId.substring(index + 1);
            System.err.print(systemId);
        }
        System.err.print(':' + ex.getLineNumber());
        System.err.print(':' + ex.getColumnNumber());
        System.err.println(": " + ex.getMessage());
        System.err.flush();
    }

    private void writeFlush(String out) {
        fOut.print(out);
        fOut.flush();
    }
}
