

package javax.xml.transform.ptests;

import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class MyContentHandler implements ContentHandler {
    
    private final BufferedWriter bWriter;

    
    public MyContentHandler(String fileName) throws SAXException {
        try {
            bWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException ex) {
            throw new SAXException("Open file error", ex);
        }
    }

    
    @Override
    public void setDocumentLocator (Locator locator) {
    }

    
    @Override
    public void startDocument () throws SAXException {
        
        
        println("startDocument");
    }

    
    @Override
    public void endDocument() throws SAXException {
        println("endDocument");

        try {
            bWriter.flush();
            bWriter.close();
        } catch (IOException ex) {
            throw new SAXException("Close file error", ex);
        }
    }

    
    @Override
    public void startPrefixMapping (String prefix, String uri)
                throws SAXException {
        println("startPrefixMapping: " + prefix + ", " + uri);
    }

    
    @Override
    public void endPrefixMapping (String prefix) throws SAXException {
        println("endPrefixMapping: " + prefix);
    }

    
    @Override
    public void startElement (String namespaceURI, String localName,
                                String qName, Attributes atts)
                        throws SAXException {
        String str = "startElement: " + namespaceURI + ", " + namespaceURI +
                         ", " + qName;
        int n = atts.getLength();
        for(int i = 0; i < n; i++) {
            str = str + ", " + atts.getQName(i);
        }

        println(str);
    }

    
    @Override
    public void endElement (String namespaceURI, String localName,
                                String qName) throws SAXException {
        println("endElement: " + namespaceURI + ", " + namespaceURI + ", " + qName);
    }


    
    @Override
    public void characters (char ch[], int start, int length)
                        throws SAXException {
        println("characters");
    }

    
    @Override
    public void ignorableWhitespace (char ch[], int start, int length)
                throws SAXException {
        println("ignorableWhitespace");
    }

    
    @Override
    public void processingInstruction (String target, String data)
                throws SAXException {
        println("processingInstruction: " + target + ", " + target);
    }

    
    @Override
    public void skippedEntity (String name) throws SAXException {
        println("skippedEntity: " + name);
    }

    
    private void println(String output) throws SAXException  {
        try {
            bWriter.write(output, 0, output.length());
            bWriter.newLine();
        } catch (IOException ex) {
            throw new SAXException("bWriter error", ex);
        }
    }
}
