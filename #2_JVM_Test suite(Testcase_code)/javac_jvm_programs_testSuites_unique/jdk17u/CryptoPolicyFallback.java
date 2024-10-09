import java.io.*;
import java.nio.file.*;
import java.util.stream.*;
import javax.crypto.*;

public class CryptoPolicyFallback {

    private static final String FILENAME = "java.security";

    public static void main(String[] args) throws Exception {
        String javaHome = System.getProperty("java.home");
        Path path = Paths.get(javaHome, "conf", "security", FILENAME);
        String defaultPolicy;
        try (Stream<String> lines = Files.lines(path)) {
            defaultPolicy = lines.filter(x -> x.startsWith("crypto.policy=")).findFirst().orElseThrow(() -> new Exception("Missing crypto.policy")).split("=")[1].trim();
        }
        try (PrintWriter out = new PrintWriter(FILENAME);
            Stream<String> lines = Files.lines(path)) {
            lines.filter(x -> !x.trim().startsWith("crypto.policy=")).forEach(out::println);
        }
        System.setProperty("java.security.properties", "=" + FILENAME);
        int expected;
        switch(defaultPolicy) {
            case "limited":
                expected = 128;
                break;
            case "unlimited":
                expected = Integer.MAX_VALUE;
                break;
            default:
                throw new Exception("Unexpected Default Policy Value: " + defaultPolicy);
        }
        int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
        System.out.println("Default Policy: " + defaultPolicy + "\nExpected max AES key length: " + expected + ", received : " + maxKeyLen);
        if (expected != maxKeyLen) {
            throw new Exception("Wrong Key Length size!");
        }
        System.out.println("PASSED!");
    }
}
