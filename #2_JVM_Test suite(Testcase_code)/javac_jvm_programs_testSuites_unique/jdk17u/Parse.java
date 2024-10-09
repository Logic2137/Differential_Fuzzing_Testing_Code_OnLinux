import javax.security.auth.x500.X500Principal;

public class Parse {

    private static TestCase[] testCases = { new TestCase("CN=prefix\\<>suffix", false), new TestCase("OID.1=value", false), new TestCase("oid.1=value", false), new TestCase("OID.1.2=value", true), new TestCase("oid.1.2=value", true), new TestCase("1=value", false), new TestCase("1.2=value", true) };

    public static void main(String[] args) throws Exception {
        for (TestCase testCase : testCases) {
            testCase.run();
        }
        System.out.println("Test completed ok.");
    }
}

class TestCase {

    private String name;

    private boolean expectedResult;

    TestCase(String name, boolean expectedResult) {
        this.name = name;
        this.expectedResult = expectedResult;
    }

    void run() throws Exception {
        Exception f = null;
        try {
            System.out.println("Parsing: \"" + name + "\"");
            new X500Principal(name);
            if (expectedResult == false) {
                f = new Exception("Successfully parsed invalid name");
            }
        } catch (IllegalArgumentException e) {
            if (expectedResult == true) {
                throw e;
            }
        }
        if (f != null) {
            throw f;
        }
    }
}
