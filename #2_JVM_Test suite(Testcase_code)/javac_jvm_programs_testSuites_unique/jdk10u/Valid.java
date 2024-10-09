

package saaj.factory;


import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;


public class Valid extends MessageFactory {
    @Override
    public SOAPMessage createMessage() throws SOAPException {
        return null;
    }

    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        return null;
    }
}
