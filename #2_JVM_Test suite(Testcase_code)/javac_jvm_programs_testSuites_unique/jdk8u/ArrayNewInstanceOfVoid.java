public class ArrayNewInstanceOfVoid {

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100_000; i++) {
            test();
        }
    }

    private static void test() {
        try {
            java.lang.reflect.Array.newInstance(void.class, 2);
        } catch (IllegalArgumentException e) {
        }
    }
}
