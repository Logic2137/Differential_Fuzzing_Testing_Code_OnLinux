import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.Transform;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;

public class UnknownProvider {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        try {
            TransformService ts = TransformService.getInstance(Transform.BASE64, "DOM", "SomeProviderThatDoesNotExist");
        } catch (NoSuchProviderException e) {
        }
    }
}
