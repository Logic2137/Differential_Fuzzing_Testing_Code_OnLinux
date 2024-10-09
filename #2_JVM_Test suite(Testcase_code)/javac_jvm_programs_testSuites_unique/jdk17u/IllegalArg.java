import java.net.*;

public class IllegalArg {

    public static void main(String[] args) throws Exception {
        String[] illegalNames = { "com..net", "com..", ".com", ".com." };
        String[] legalNames = { "example.com", "com\u3002", "com.", "." };
        for (String name : illegalNames) {
            try {
                IDN.toASCII(name, IDN.USE_STD3_ASCII_RULES);
                throw new Exception("Expected to get IllegalArgumentException for " + name);
            } catch (IllegalArgumentException iae) {
            }
            try {
                IDN.toASCII(name);
                throw new Exception("Expected to get IllegalArgumentException for " + name);
            } catch (IllegalArgumentException iae) {
            }
        }
        for (String name : legalNames) {
            System.out.println("Convering " + name);
            System.out.println(IDN.toASCII(name, IDN.USE_STD3_ASCII_RULES));
        }
    }
}
