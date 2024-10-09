public class VolatileGuardTest {

    volatile static private int a;

    static private int b;

    static void test() {
        int tt = b;
        while (a == 0) {
        }
        if (b == 0) {
            System.err.println("wrong value of b");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(VolatileGuardTest::test).start();
        }
        b = 1;
        a = 1;
    }
}
