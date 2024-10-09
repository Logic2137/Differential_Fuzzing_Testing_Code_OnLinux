



import sun.security.x509.BasicConstraintsExtension;
import java.io.ByteArrayOutputStream;

public class BCNull {
    public static void main(String [] args) throws Exception {
        new BasicConstraintsExtension(false, -1).encode(new ByteArrayOutputStream());
    }
}
