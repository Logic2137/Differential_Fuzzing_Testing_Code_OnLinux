


public class SecurityExceptions {
    public static void main(String[] args) {
        boolean expectException = Boolean.parseBoolean(args[0]);

        StackWalker sw = StackWalker.getInstance();

        try {
            sw = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
            if (expectException) {
                throw new RuntimeException("Expected SecurityException, but none thrown");
            }
        } catch (SecurityException e) {
            if (!expectException) {
                System.err.println("Unexpected security exception:");
                throw e;
            }
        }
    }
}
