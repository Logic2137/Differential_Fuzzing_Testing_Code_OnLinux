
package org.xml.sax.ptests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MyAttrCHandler extends DefaultHandler {
    
    private final BufferedWriter bWriter;

    
    public MyAttrCHandler(String fileName) throws IOException {
        bWriter = new BufferedWriter(new FileWriter(fileName));
    }

    
    @Override
    public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
        try {
            String string = "uri <" + uri + "> localName <" + localName +
                        "> qName <" + qName + ">";

            bWriter.write( string, 0, string.length());
            bWriter.newLine();

            int length = attributes.getLength();
            string = "length: " + length;

            bWriter.write( string, 0, string.length());
            bWriter.newLine();

            for (int ind=0; ind < length ; ind++) {
                string = "For index = " + ind + "\n";
                string += "getLocalName <" + attributes.getLocalName(ind)
                                +">" + "\n";
                string += "getQName <" + attributes.getQName(ind) +">" + "\n";
                string += "getType <" + attributes.getType(ind) +">" + "\n";
                string += "getURI <" + attributes.getURI(ind) +">" + "\n";
                string += "getValue <" + attributes.getValue(ind) +">" + "\n";

                bWriter.write( string, 0, string.length());
                bWriter.newLine();

                String gotLocalName = attributes.getLocalName(ind);
                String gotQName = attributes.getQName(ind);
                String gotURI = attributes.getURI(ind);

                string ="Using localName, qname and uri pertaining to index = "
                                + ind;
                bWriter.write( string, 0, string.length());
                bWriter.newLine();

                string = "getIndex(qName) <" + attributes.getIndex(gotQName)
                                +">" + "\n";
                string += "getIndex(uri, localName) <" +
                        attributes.getIndex(gotURI, gotLocalName) +">" + "\n";

                string += "getType(qName) <" +
                        attributes.getType(gotQName) +">" + "\n";
                string += "getType(uri, localName) <" +
                        attributes.getType(gotURI, gotLocalName) +">" + "\n";

                string += "getValue(qName) <" +
                        attributes.getValue(gotQName) +">" + "\n";
                string += "getValue(uri, localName) <" +
                        attributes.getValue(gotURI, gotLocalName) +">" + "\n";

                bWriter.write( string, 0, string.length());
                bWriter.newLine();
            }
            bWriter.newLine();
        } catch(IOException ex){
            throw new SAXException(ex);
        }
    }

    
    public void flushAndClose() throws IOException {
        bWriter.flush();
        bWriter.close();
    }
}
