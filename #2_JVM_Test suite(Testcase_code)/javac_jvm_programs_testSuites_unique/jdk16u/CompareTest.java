




package compiler.intrinsics.mathexact;

public class CompareTest {
    public static long store = 0;
    public static long addValue = 1231;

    public static void main(String[] args) {
        for (int i = 0; i < 20000; ++i) {
            runTest(i, i);
            runTest(i-1, i);
        }
    }

    public static long create(long value, int v) {
        if ((value | v) == 0) {
            return 0;
        }

        
        if (value < -31557014167219200L || value > 31556889864403199L) {
            throw new RuntimeException("error");
        }

        return value;
    }

    public static void runTest(long value, int value2) {
        long res = Math.addExact(value, addValue);
        store = create(res, Math.floorMod(value2, 100000));
    }
}
