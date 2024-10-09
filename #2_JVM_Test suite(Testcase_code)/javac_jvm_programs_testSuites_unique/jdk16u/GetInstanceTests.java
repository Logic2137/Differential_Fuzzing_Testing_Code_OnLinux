


import java.security.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;


public class GetInstanceTests {

    public static void main(String[] argv) throws Exception {
        TestTransformService(CanonicalizationMethod.INCLUSIVE, "DOM");
        TestTransformService(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, "DOM");
        TestTransformService(Transform.BASE64, "DOM");
        TestTransformService(Transform.XPATH2, "DOM");
        TestXMLSignatureFactory();
        TestKeyInfoFactory();
    }

    private static void TestTransformService(String algo,
        String mechType) throws Exception {
        TransformService ts = TransformService.getInstance(algo, mechType);
        Provider p = ts.getProvider();
        try {
            ts = TransformService.getInstance(algo, mechType, p);
            ts = TransformService.getInstance(algo, mechType, p.getName());
        } catch (Exception ex) {
            throw new RuntimeException("Error: Unexpected exception", ex);
        }
    }

    private static void TestXMLSignatureFactory() throws Exception {
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance();
        Provider p = fac.getProvider();
        String mechType = fac.getMechanismType();
        Provider p2;
        try {
            fac = XMLSignatureFactory.getInstance(mechType);
            p2 = fac.getProvider();
            fac = XMLSignatureFactory.getInstance(mechType, p);
            fac = XMLSignatureFactory.getInstance(mechType, p.getName());
        } catch (Exception ex) {
            throw new RuntimeException("Error: Unexpected exception", ex);
        }
        if (p2.getName() != p.getName()) {
            throw new RuntimeException("Error: Provider equality check failed");
        }
        if (p2.getName() != p.getName()) {
            throw new RuntimeException("Error: Provider equality check failed");
        }
    }

    private static void TestKeyInfoFactory() throws Exception {
        KeyInfoFactory fac = KeyInfoFactory.getInstance();
        Provider p = fac.getProvider();
        String mechType = fac.getMechanismType();
        Provider p2;
        try {
            fac = KeyInfoFactory.getInstance(mechType);
            p2 = fac.getProvider();
            fac = KeyInfoFactory.getInstance(mechType, p);
            fac = KeyInfoFactory.getInstance(mechType, p.getName());
        } catch (Exception ex) {
            throw new RuntimeException("Error: Unexpected exception", ex);
        }
        if (p2.getName() != p.getName()) {
            throw new RuntimeException("Error: Provider equality check failed");
        }
    }
}
