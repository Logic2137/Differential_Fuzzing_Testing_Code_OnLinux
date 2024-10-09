

package transform;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class MyXMLStreamReader extends XMLStreamReaderImpl implements XMLStreamReader {

    public MyXMLStreamReader(InputStream inputStream, PropertyManager props) throws XMLStreamException {
        super(inputStream, props);
    }

    public MyXMLStreamReader(XMLInputSource inputSource, PropertyManager props)
            throws XMLStreamException {
        super(inputSource, props);
    }

    public String getAttributeType(int index) {
        return null;
    }
}
