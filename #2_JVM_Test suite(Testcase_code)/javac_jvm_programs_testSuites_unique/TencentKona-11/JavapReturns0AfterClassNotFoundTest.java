



import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class JavapReturns0AfterClassNotFoundTest {

    static final String fileNotFoundErrorMsg =
            "Error: class not found: Unexisting.class";
    static final String exitCodeClassNotFoundAssertionMsg =
            "Javap's exit code for class not found should be 1";
    static final String classNotFoundMsgAssertionMsg =
            "Javap's error message for class not found is incorrect";

    public static void main(String args[]) throws Exception {
        new JavapReturns0AfterClassNotFoundTest().run();
    }

    void run() throws IOException {
        check(exitCodeClassNotFoundAssertionMsg, classNotFoundMsgAssertionMsg,
                fileNotFoundErrorMsg, "-v", "Unexisting.class");
    }

    void check(String exitCodeAssertionMsg, String errMsgAssertionMsg,
            String expectedErrMsg, String... params) {
        int result;
        StringWriter s;
        String out;
        try (PrintWriter pw = new PrintWriter(s = new StringWriter())) {
            result = com.sun.tools.javap.Main.run(params, pw);
            
            out = s.toString().trim();
        }
        if (result != 1) {
            System.out.println("actual exit code " + result);
            throw new AssertionError(exitCodeAssertionMsg);
        }

        if (!out.equals(expectedErrMsg)) {
            System.out.println("actual " + out);
            System.out.println("expected " + expectedErrMsg);
            throw new AssertionError(errMsgAssertionMsg);
        }
    }

}
