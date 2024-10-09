import java.security.PKCS12Attribute;

public class HashCode {

    public static void main(String[] args) throws Exception {
        int h1 = new PKCS12Attribute("1.2.3.4", "AA").hashCode();
        int h2 = new PKCS12Attribute("2.3.4.5", "BB,CC").hashCode();
        if (h1 == -1 || h2 == -1 || h1 == h2) {
            throw new Exception("I see " + h1 + " and " + h2);
        }
    }
}
