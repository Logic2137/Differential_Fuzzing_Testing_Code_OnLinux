public class TestProfileReturnTypePrinting {

    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERATIONS; i++) {
            TestProfileReturnTypePrinting.testMethod(i);
        }
    }

    private static int testMethod(int i) {
        return TestProfileReturnTypePrinting.foo().hashCode() + TestProfileReturnTypePrinting.bar(i).hashCode();
    }

    private static B foo() {
        return new B();
    }

    private static Object bar(int i) {
        if (i % 2 == 0) {
            return new A();
        } else {
            return new B();
        }
    }

    private static class A {
    }

    private static class B extends A {
    }
}
