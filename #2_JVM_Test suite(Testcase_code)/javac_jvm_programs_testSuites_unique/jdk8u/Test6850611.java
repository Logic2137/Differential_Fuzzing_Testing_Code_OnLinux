public class Test6850611 {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < 5; ++j) {
            long x = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; ++i) {
                x += i;
            }
            System.out.println("sum: " + x);
            if (x != -4294967295l) {
                System.out.println("FAILED");
                System.exit(97);
            }
        }
    }
}
