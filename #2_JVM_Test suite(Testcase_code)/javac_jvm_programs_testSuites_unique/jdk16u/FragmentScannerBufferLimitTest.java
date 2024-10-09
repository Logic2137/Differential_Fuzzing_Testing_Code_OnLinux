


import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import org.xml.sax.*;


public class FragmentScannerBufferLimitTest {

    static int errCount = 0;

    
    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, TransformerConfigurationException,
            TransformerException, TransformerFactoryConfigurationError {

        String testString = "<TEST>content</TEST><TEST2>content2</TEST2>";

        for (int i = 0; i < testString.length(); i++) {
            test(createDocument(testString.toString(), i), ""+ i);
        }

        if (errCount == 0) {
            System.out.println("OK");
        }
        else {
            System.out.println("ERROR");
            throw new RuntimeException("Parsing error: element content has been overwritten");
        }
    }

    
    private static String createDocument(String testString, int bufferLimitPosition) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.1\"?>");
        result.append("<ROOT>");

        int fillerLength = 8192 - bufferLimitPosition;
        createFiller(result, fillerLength);

        result.append(testString);

        createFiller(result, 9000);
        result.append("</ROOT>");
        return result.toString();
    }

    
    private static void createFiller(StringBuilder buffer, int length) {
        buffer.append("<FILLER>");
        int fillLength = length - "<FILLER></FILLER>".length();
        for (int i=0; i<fillLength; i++) {
            buffer.append('f');
        }
        buffer.append("</FILLER>");
    }


    private static void test(String document, String testName) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new ByteArrayInputStream(document.getBytes("UTF-8")));

        
        NodeList roots = doc.getElementsByTagName("ROOT");
        assert roots.getLength() == 1;
        Node root = roots.item(0);

        
        NodeList children = root.getChildNodes();
        assert children.getLength() == 4;
        assert children.item(0).getNodeName().equals("FILLER");
        assert children.item(1).getNodeName().equals("TEST");
        assert children.item(2).getNodeName().equals("TEST2");
        assert children.item(3).getNodeName().equals("FILLER");

        
        checkContent(children.item(1).getTextContent(), "content", document);
        checkContent(children.item(2).getTextContent(), "content2", document);
    }

    private static void checkContent(String found, String expected, String document) {
        if (! (found.equals(expected))) {
            errCount++;
            int bufferStart = "<?xml version=\"1.1\"?><ROOT>".length() +1;
            int bufferStart2 = bufferStart + 8192;
            System.err.println("\nError:: expected \"" + expected
                    + "\", but found \"" + found + "\"!");
            System.err.println("Buffer was (probably): [ ... "
                    + document.substring(bufferStart2 - 20, bufferStart2) + "] ["
                    + document.substring(bufferStart2, bufferStart2 + 30) + " ... ]");
        }
    }
}
