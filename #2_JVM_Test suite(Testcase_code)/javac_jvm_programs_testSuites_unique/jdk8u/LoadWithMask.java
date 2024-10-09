public class LoadWithMask {

    static int[] x = new int[1];

    static long foo() {
        return x[0] & 0xfff0ffff;
    }

    public static void main(String[] args) {
        x[0] = -1;
        long l = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; ++i) {
            l = foo();
        }
    }
}
