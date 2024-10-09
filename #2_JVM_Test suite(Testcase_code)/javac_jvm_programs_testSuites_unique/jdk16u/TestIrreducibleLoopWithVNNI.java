



package compiler.loopopts;

public class TestIrreducibleLoopWithVNNI {
    public static boolean condition = false;

    public static void main(String[] strArr) {
        TestIrreducibleLoopWithVNNI _instance = new TestIrreducibleLoopWithVNNI();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest();
        }
    }

    public void mainTest() {
        int a = 1, b = 1, c = 1, d = 51;
        for (b = 0; b < 100; b++) {
            a = a + b * 342;
            for (c = 0; c < 100; c++) {
                for (d = 0; d < 1; d++)
                    a = d;
                if (condition)
                    break;
            }
            a = d * a;
        }
    }
}
