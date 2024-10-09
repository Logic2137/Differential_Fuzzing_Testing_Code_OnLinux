


import javax.crypto.*;
import java.security.Security;
import java.nio.file.*;
import java.util.stream.*;

public class TestUnlimited {

    private enum Result {
        UNLIMITED,
        LIMITED,
        EXCEPTION,
        UNKNOWN
    };

    
    private static String getDefaultPolicy() throws Exception {
        String javaHome = System.getProperty("java.home");
        Path path = Paths.get(javaHome, "conf", "security", "java.security");

        try (Stream<String> lines = Files.lines(path)) {
            return lines.filter(x -> x.startsWith("crypto.policy="))
                    .findFirst().orElseThrow(
                            () -> new Exception("Missing crypto.policy"))
                    .split("=")[1].trim();
        }
    }

    public static void main(String[] args) throws Exception {
        
        if (args.length != 2) {
            throw new Exception("Two args required");
        }

        String testStr = args[0];
        String expectedStr = args[1];
        if (testStr.equals("use_default")) {
            expectedStr = getDefaultPolicy();
        }

        Result expected = Result.UNKNOWN;  
        Result result;

        switch (expectedStr) {
        case "unlimited":
            expected = Result.UNLIMITED;
            break;
        case "limited":
            expected = Result.LIMITED;
            break;
        case "exception":
            expected = Result.EXCEPTION;
            break;
        default:
            throw new Exception("Unexpected argument");
        }

        System.out.println("Testing: " + testStr);
        if (testStr.equals("\"\"")) {
            Security.setProperty("crypto.policy", "");
        } else {
            
            if (!testStr.equals("use_default")) {
                Security.setProperty("crypto.policy", testStr);
            }
        }

        
        try {
            int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
            System.out.println("max AES key len:" + maxKeyLen);
            if (maxKeyLen > 128) {
                System.out.println("Unlimited policy is active");
                result = Result.UNLIMITED;
            } else {
                System.out.println("Unlimited policy is NOT active");
                result = Result.LIMITED;
            }
        } catch (Throwable e) {
            
            result = Result.EXCEPTION;
        }

        System.out.println(
                "Expected:\t" + expected + "\nResult:\t\t" + result);
        if (!expected.equals(result)) {
            throw new Exception("Didn't match");
        }

        System.out.println("DONE!");
    }
}
