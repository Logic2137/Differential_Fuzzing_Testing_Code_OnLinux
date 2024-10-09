
package compiler.escapeAnalysis;

public class TestEliminateAllocation {

    public static int a = 20;

    public static int b = 30;

    public static int c = 40;

    public void test() {
        int i = 0;
        do {
            int[] arr = new int[] { a / b / c };
            Wrapper wrapper = new Wrapper();
            wrapper.setArr(arr);
            i++;
        } while (i < 10);
    }

    public static void main(String[] strArr) {
        TestEliminateAllocation _instance = new TestEliminateAllocation();
        for (int i = 0; i < 10_000; i++) {
            _instance.test();
        }
    }
}

class Wrapper {

    int[] arr;

    void setArr(int... many) {
        arr = many;
    }
}
