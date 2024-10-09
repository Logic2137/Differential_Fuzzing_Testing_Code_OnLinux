


public class TestSyntheticNullChecks {

    class Inner { }

    static void generateSyntheticNPE(TestSyntheticNullChecks outer) {
        outer.new Inner(); 
    }

    public static void main(String[] args) {
        int version = Integer.valueOf(args[0]);
        boolean useObjects = version >= 7;
        try {
            generateSyntheticNPE(null);
        } catch (NullPointerException npe) {
            boolean hasRequireNotNull = false;
            for (StackTraceElement e : npe.getStackTrace()) {
                if (e.getClassName().equals("java.util.Objects") &&
                        e.getMethodName().equals("requireNonNull")) {
                    hasRequireNotNull = true;
                    break;
                }
            }
            if (hasRequireNotNull != useObjects) {
                throw new AssertionError();
            }
        }
    }
}
