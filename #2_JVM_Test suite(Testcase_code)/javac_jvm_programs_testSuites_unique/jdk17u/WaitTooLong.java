public class WaitTooLong {

    public static void main(String[] args) {
        test(0);
        test(1);
        test(500000);
        test(999999);
    }

    static void test(int nanos) {
        try {
            WaitTooLong.class.wait(Long.MAX_VALUE, nanos);
        } catch (IllegalMonitorStateException expected) {
        } catch (IllegalArgumentException | InterruptedException unexpected) {
            throw new RuntimeException("Unexpected", unexpected);
        }
    }
}
