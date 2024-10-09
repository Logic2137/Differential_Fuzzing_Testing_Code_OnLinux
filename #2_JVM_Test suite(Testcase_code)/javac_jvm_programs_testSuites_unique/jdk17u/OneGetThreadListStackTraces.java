public class OneGetThreadListStackTraces {

    private static native void checkCallStacks(Thread thread);

    public static void main(String[] args) throws Exception {
        checkCallStacks(Thread.currentThread());
    }
}
