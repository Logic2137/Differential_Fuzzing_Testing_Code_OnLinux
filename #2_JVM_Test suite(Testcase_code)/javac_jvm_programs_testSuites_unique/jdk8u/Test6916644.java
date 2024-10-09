public class Test6916644 {

    static int result;

    static int i1;

    static int i2;

    static public void test(double d) {
        result = (d <= 0.0D) ? i1 : i2;
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; i++) {
            test(i & 1);
        }
    }
}
