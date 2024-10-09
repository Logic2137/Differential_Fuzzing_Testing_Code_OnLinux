


public class ExpectedEncoding {
    public static void main(String[] args) {
        boolean failed = false;
        if (args.length != 2) {
            System.out.println("Usage:");
            System.out.println("$ java ExpectedEncoding <expected file.encoding> <expected sun.jnu.encoding>");
            System.out.println("$   use \"skip\" to skip checking property's value");
            System.exit(1);
        }
        String expectFileEnc = args[0];
        String expectSunJnuEnc = args[1];

        String fileEnc = System.getProperty("file.encoding");
        String jnuEnc = System.getProperty("sun.jnu.encoding");

        if ("skip".equals(expectFileEnc)) {
            System.err.println("Expected file.encoding is \"skip\", ignoring");
        } else {
            if (fileEnc == null || !fileEnc.equals(expectFileEnc)) {
                System.err.println("Expected file.encoding: " + expectFileEnc);
                System.err.println("Actual file.encoding: " + fileEnc);
                failed = true;
            }
        }
        if ("skip".equals(expectSunJnuEnc)) {
            System.err.println("Expected sun.jnu.encoding is \"skip\", ignoring");
        } else {
            if (jnuEnc == null || !jnuEnc.equals(expectSunJnuEnc)) {
                System.err.println("Expected sun.jnu.encoding: " + expectSunJnuEnc);
                System.err.println("Actual sun.jnu.encoding: " + jnuEnc);
                failed = true;
            }
        }

        if (failed) {
            throw new RuntimeException("Test Failed");
        }
    }
}
