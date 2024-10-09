
package fib;

public class FibJNI {

    static {
        System.loadLibrary("Fib");
    }

    private final int num;

    private final long expected;

    public FibJNI(int num, long expected) {
        this.num = num;
        this.expected = expected;
    }

    public void callNative() {
        callJNI(this, num);
    }

    private void callback(long val) {
        System.out.println("Debug: result was: " + val);
        if (val != expected) {
            throw new RuntimeException("Expected " + expected + " but got: " + val);
        }
    }

    public static native void callJNI(Object target, int num);

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: " + FibJNI.class.getName() + " <input> <expectedResult>");
        }
        int input = Integer.parseInt(args[0]);
        long expected = Long.parseLong(args[1]);
        FibJNI fib = new FibJNI(input, expected);
        fib.callNative();
        System.out.println("DEBUG: Sanity check for " + FibJNI.class.getSimpleName() + " passed.");
    }
}
