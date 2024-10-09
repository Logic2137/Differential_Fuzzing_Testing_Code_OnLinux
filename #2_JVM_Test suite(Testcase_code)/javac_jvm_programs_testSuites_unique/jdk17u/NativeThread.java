public class NativeThread {

    public static final int SIGPIPE;

    static {
        SIGPIPE = getSIGPIPE();
    }

    public static native long getID();

    public static native int signal(long threadId, int sig);

    private static native int getSIGPIPE();
}
