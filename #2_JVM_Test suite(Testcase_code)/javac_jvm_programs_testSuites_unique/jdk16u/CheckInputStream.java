



import java.io.*;
import java.security.*;

public class CheckInputStream {
    private final static String DIR = System.getProperty("test.src", ".");
    private static final char[] PASSWORD = "passphrase".toCharArray();
    private static final String KEYSTORE = DIR + "/keystore.jks";

    public static final void main(String[] args) throws Exception {

        KeyStore keystore = KeyStore.getInstance("JKS");
        try (FileInputStream inStream = new FileInputStream(KEYSTORE)) {
            System.out.println("Loading JKS keystore: " + KEYSTORE);
            keystore.load(inStream, PASSWORD);
            
            inStream.available();
            System.out.println("OK");
        }
    }
}
