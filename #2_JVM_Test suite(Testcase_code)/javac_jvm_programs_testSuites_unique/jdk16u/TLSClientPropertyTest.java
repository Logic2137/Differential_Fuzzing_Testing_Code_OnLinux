



import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;


public class TLSClientPropertyTest {
    private final String[] expectedSupportedProtos = new String[] {
            "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"
    };

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            throw new RuntimeException(
                    "Incorrect arguments,expected arguments: testCase");
        }

        String[] expectedDefaultProtos;
        String testCase = args[0];
        String contextProtocol;
        switch (testCase) {
        case "NoProperty":
            if (System.getProperty("jdk.tls.client.protocols") != null) {
                System.getProperties().remove("jdk.tls.client.protocols");
            }
            contextProtocol = null;
            expectedDefaultProtos = new String[] {
                    "TLSv1.2", "TLSv1.3"
            };
            break;
        case "SSLv3":
            contextProtocol = "SSLv3";
            expectedDefaultProtos = new String[] {
            };
            break;
        case "TLSv1":
            contextProtocol = "TLSv1";
            expectedDefaultProtos = new String[] {
            };
            break;
        case "TLSv11":
            contextProtocol = "TLSv1.1";
            expectedDefaultProtos = new String[] {
            };
            break;
        case "TLSv12":
            contextProtocol = "TLSv1.2";
            expectedDefaultProtos = new String[] {
                    "TLSv1.2"
            };
            break;
        case "TLSv13":
        case "TLS":
            contextProtocol = "TLSv1.3";
            expectedDefaultProtos = new String[] {
                    "TLSv1.2", "TLSv1.3"
            };
            break;
        case "WrongProperty":
            expectedDefaultProtos = new String[] {};
            contextProtocol = "TLSV";
            break;
        default:
            throw new RuntimeException("test case is wrong");
        }
        if (contextProtocol != null) {
            System.setProperty("jdk.tls.client.protocols", contextProtocol);
        }
        try {
            TLSClientPropertyTest test = new TLSClientPropertyTest();
            test.test(contextProtocol, expectedDefaultProtos);
            if (testCase.equals("WrongProperty")) {
                throw new RuntimeException(
                        "Test failed: NoSuchAlgorithmException " +
                        "is expected when input wrong protocol");
            } else {
                System.out.println("Test " + contextProtocol + " passed");
            }
        } catch (NoSuchAlgorithmException nsae) {
            if (testCase.equals("WrongProperty")) {
                System.out.println("NoSuchAlgorithmException is expected,"
                        + contextProtocol + " test passed");
            } else {
                throw nsae;
            }
        }

    }

    
    public void test(String expectedContextProto,
            String[] expectedDefaultProtos) throws NoSuchAlgorithmException {

        SSLContext context = null;
        try {
            if (expectedContextProto != null) {
                context = SSLContext.getInstance(expectedContextProto);
                context.init(null, null, null);
            } else {
                context = SSLContext.getDefault();
            }
            printContextDetails(context);
        } catch (KeyManagementException ex) {
            error(null, ex);
        }

        validateContext(expectedContextProto, expectedDefaultProtos, context);
    }

    
    private void printContextDetails(SSLContext context) {
        System.out.println("Default   Protocols: "
                + Arrays.toString(context.getDefaultSSLParameters()
                        .getProtocols()));
        System.out.println("Supported Protocols: "
                + Arrays.toString(context.getSupportedSSLParameters()
                        .getProtocols()));
        System.out.println("Current   Protocol : " + context.getProtocol());

    }

    
    private void error(String msg, Throwable tble) {
        String finalMsg = "FAILED " + (msg != null ? msg : "");
        if (tble != null) {
            throw new RuntimeException(finalMsg, tble);
        }
        throw new RuntimeException(finalMsg);
    }

    
    private void validateContext(String expectedProto,
            String[] expectedDefaultProtos, SSLContext context) {
        if (expectedProto == null) {
            expectedProto = "Default";
        }
        if (!context.getProtocol().equals(expectedProto)) {
            error("Invalid current protocol: " + context.getProtocol()
                    + ", Expected:" + expectedProto, null);
        }
        List<String> actualDefaultProtos = Arrays.asList(context
                .getDefaultSSLParameters().getProtocols());
        for (String p : expectedDefaultProtos) {
            if (!actualDefaultProtos.contains(p)) {
                error("Default protocol " + p + "missing", null);
            }
        }
        List<String> actualSupportedProtos = Arrays.asList(context
                .getSupportedSSLParameters().getProtocols());

        for (String p : expectedSupportedProtos) {
            if (!actualSupportedProtos.contains(p)) {
                error("Expected to support protocol:" + p, null);
            }
        }
    }
}
