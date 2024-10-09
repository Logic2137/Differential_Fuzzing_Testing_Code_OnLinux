import javax.net.ssl.SNIHostName;

public class IllegalSNIName {

    public static void main(String[] args) throws Exception {
        String[] illegalNames = { "example\u3002\u3002com", "example..com", "com\u3002", "com.", "." };
        for (String name : illegalNames) {
            try {
                SNIHostName hostname = new SNIHostName(name);
                throw new Exception("Expected to get IllegalArgumentException for " + name);
            } catch (IllegalArgumentException iae) {
            }
        }
    }
}
