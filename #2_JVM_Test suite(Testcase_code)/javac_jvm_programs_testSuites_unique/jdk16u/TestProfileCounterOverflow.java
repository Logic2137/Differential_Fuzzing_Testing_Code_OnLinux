



package compiler.profiling;

public class TestProfileCounterOverflow {
    public static void test(long iterations) {
        for (long j = 0; j < iterations; j++) {
            call();
        }
    }

    public static void call() {}

    public static void main(String[] args) {
        
        for (int i = 0; i < 500; i++) {
            test(1);
        }

        test(Integer.MAX_VALUE + 10000L); 

        
        for (int i = 0; i < 10_000; i++) {
            test(1);
        }
        System.out.println("TEST PASSED");
    }
}
