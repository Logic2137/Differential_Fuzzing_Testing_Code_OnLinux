public class FinInterrupt {

    public static void main(String[] args) throws Exception {
        Thread.currentThread().interrupt();
        System.runFinalization();
        if (Thread.interrupted()) {
            System.out.println("Passed: interrupt bit was still set.");
        } else {
            throw new AssertionError("interrupt bit was cleared");
        }
    }
}
