public class TestCommonGCLoads {

    static Object d = new Object();

    static Target t1 = new Target();

    static Target t2 = new Target();

    static Target t3 = new Target();

    static Target t4 = new Target();

    static Target t5 = new Target();

    static void test() {
        t1.field = d;
        t2.field = d;
        t3.field = d;
        t4.field = d;
        t5.field = d;
    }

    static public void main(String[] args) {
        for (int i = 0; i < 100_000; i++) {
            test();
        }
    }

    static class Target {

        Object field;
    }
}
