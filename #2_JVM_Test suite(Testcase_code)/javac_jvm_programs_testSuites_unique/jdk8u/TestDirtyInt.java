public class TestDirtyInt {

    static {
        System.loadLibrary("TestDirtyInt");
    }

    native static int test(int v);

    static int compiled(int v) {
        return test(v << 2);
    }

    static public void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            int res = compiled(Integer.MAX_VALUE);
            if (res != 0x42) {
                throw new RuntimeException("Test failed");
            }
        }
    }
}
