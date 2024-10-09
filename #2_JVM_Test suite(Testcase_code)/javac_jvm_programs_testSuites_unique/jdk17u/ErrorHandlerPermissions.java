import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.xml.XMLConstants;
import javax.xml.crypto.Data;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ErrorHandlerPermissions {

    private final static String FS = System.getProperty("file.separator");

    private final static String DIR = System.getProperty("test.src", ".");

    private final static String DATA_DIR = DIR + FS + "data";

    private final static String SIGNATURE = DATA_DIR + FS + "signature-external-rsa.xml";

    private static final String validationKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnx4TdvPSA5vcsPi0OJZi9Ox0Z" + "2FRz2oeUCtuWoyEg0kUCeFd+jJZMstDJUiZNSOeuCO3FWSpdJgAwI4zlveHvuU/o" + "qHSa1eYTObOCvxfVYGGflWsSvGXyiANtRWVUrYODBeyL+2pWxDYh+Fi5EKizPfTG" + "wRjBVRSkRZKTnSjnQwIDAQAB";

    private static final URIDereferencer dereferencer = new DummyURIDereferencer();

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        Document doc = dbf.newDocumentBuilder().parse(new File(SIGNATURE));
        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nl.getLength() == 0) {
            throw new RuntimeException("Couldn't find 'Signature' element");
        }
        Element element = (Element) nl.item(0);
        byte[] keyBytes = Base64.getDecoder().decode(validationKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);
        KeySelector ks = KeySelector.singletonKeySelector(key);
        DOMValidateContext vc = new DOMValidateContext(ks, element);
        vc.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.FALSE);
        vc.setURIDereferencer(dereferencer);
        XMLSignatureFactory factory = XMLSignatureFactory.getInstance();
        XMLSignature signature = factory.unmarshalXMLSignature(vc);
        signature.validate(vc);
    }

    private static class DummyURIDereferencer implements URIDereferencer {

        @Override
        public Data dereference(final URIReference ref, XMLCryptoContext ctx) throws URIReferenceException {
            return new OctetStreamData(new ByteArrayInputStream("<test>test</test>".getBytes()), ref.getURI(), ref.getType());
        }
    }
}
