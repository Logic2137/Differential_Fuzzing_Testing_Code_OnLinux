



public class TestEliminateLocksOffCrash {
    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            try {
                test();
            } catch (Exception e) {
            }
        }
    }

    private static void test() throws Exception {
        Object obj = new Object();
        synchronized (obj) {
            throw new Exception();
        }
    }
}
