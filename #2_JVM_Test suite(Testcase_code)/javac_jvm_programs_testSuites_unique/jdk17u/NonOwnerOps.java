public class NonOwnerOps {

    public static void main(String[] args) {
        int error_count = 0;
        Object obj;
        obj = new Object();
        try {
            obj.wait();
            System.err.println("ERROR: wait() by non-owner thread did not " + "throw IllegalMonitorStateException.");
            error_count++;
        } catch (InterruptedException ie) {
            System.err.println("ERROR: wait() by non-owner thread threw " + "InterruptedException which is not expected.");
            error_count++;
        } catch (IllegalMonitorStateException imse) {
            System.out.println("wait() by non-owner thread threw the " + "expected IllegalMonitorStateException:");
            System.out.println("    " + imse);
        }
        obj = new Object();
        try {
            obj.notify();
            System.err.println("ERROR: notify() by non-owner thread did not " + "throw IllegalMonitorStateException.");
            error_count++;
        } catch (IllegalMonitorStateException imse) {
            System.out.println("notify() by non-owner thread threw the " + "expected IllegalMonitorStateException:");
            System.out.println("    " + imse);
        }
        obj = new Object();
        try {
            obj.notifyAll();
            System.err.println("ERROR: notifyAll() by non-owner thread did " + "not throw IllegalMonitorStateException.");
            error_count++;
        } catch (IllegalMonitorStateException imse) {
            System.out.println("notifyAll() by non-owner thread threw the " + "expected IllegalMonitorStateException:");
            System.out.println("    " + imse);
        }
        if (error_count != 0) {
            throw new RuntimeException("Test failed with " + error_count + " errors.");
        }
        System.out.println("Test PASSED.");
    }
}
