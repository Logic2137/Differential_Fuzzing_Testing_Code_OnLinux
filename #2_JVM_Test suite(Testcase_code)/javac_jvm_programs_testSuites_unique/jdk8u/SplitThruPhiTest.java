public class SplitThruPhiTest {

    public static volatile int value = 19;

    public static int store = 0;

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 150000; ++i) {
            store = runTest(value);
        }
    }

    public static int runTest(int val) {
        int result = Math.addExact(val, 1);
        int total = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = val; i < 200; i = Math.addExact(i, 1)) {
            total += i;
        }
        return total;
    }
}
