import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.TransformService;

public class NullParent {

    public static void main(String[] args) throws Exception {
        String[] transforms = new String[] { Transform.BASE64, Transform.ENVELOPED, Transform.XPATH, Transform.XPATH2, Transform.XSLT, CanonicalizationMethod.EXCLUSIVE, CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, CanonicalizationMethod.INCLUSIVE, CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS };
        for (String transform : transforms) {
            System.out.println("Testing " + transform);
            TransformService ts = TransformService.getInstance(transform, "DOM");
            try {
                ts.init(null, null);
                throw new Exception("init must throw NullPointerException " + "when the parent parameter is null");
            } catch (NullPointerException npe) {
            }
            try {
                ts.marshalParams(null, null);
                throw new Exception("marshalParams must throw " + "NullPointerException when the parent " + "parameter is null");
            } catch (NullPointerException npe) {
            }
        }
    }
}
