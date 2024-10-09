




public class GetLocalVars {
    private static final String agentLib = "GetLocalVars";

    static native void testLocals(Thread thread);
    static native int getStatus();

    public static
    void main(String[] args) throws Exception {
        try {
            System.loadLibrary(agentLib);
        } catch (UnsatisfiedLinkError ex) {
            System.err.println("Failed to load " + agentLib + " lib");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ex;
        }
        run(args);
        int status = getStatus();
        if (status != 0) {
            throw new RuntimeException("Test GetLocalVars failed with a bad status: " + status);
        }
    }

    public static
    void run(String argv[]) {
        GetLocalVars testedObj = new GetLocalVars();
        double pi = 3.14d;
        byte sym = 'X';
        int year = 2018;

        staticMeth(sym, testedObj, pi, year);
    }

    public static synchronized
    int staticMeth(byte byteArg, Object objArg, double dblArg, int intArg) {
        testLocals(Thread.currentThread());
        {
            int intLoc = 9999;
            intArg = intLoc;
        }
        return intArg;
    }
}
