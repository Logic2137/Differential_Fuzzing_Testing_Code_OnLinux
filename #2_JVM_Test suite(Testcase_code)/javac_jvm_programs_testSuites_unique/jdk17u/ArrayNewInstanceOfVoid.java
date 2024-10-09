
package compiler.reflection;

public class ArrayNewInstanceOfVoid {

    public static void main(String[] args) {
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
