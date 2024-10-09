



package compiler.membars;

public class DekkerTest {

    

    static final int ITERATIONS = 1000000;

    static class TestData {
        public volatile int a;
        public volatile int b;
    }

    static class ResultData {
        public int a;
        public int b;
    }

    TestData[]   testDataArray;
    ResultData[] results;

    volatile boolean start;

    public DekkerTest() {
        testDataArray = new TestData[ITERATIONS];
        results = new ResultData[ITERATIONS];
        for (int i = 0; i < ITERATIONS; ++i) {
            testDataArray[i] = new TestData();
            results[i] = new ResultData();
        }
        start = false;
    }

    public void reset() {
        for (int i = 0; i < ITERATIONS; ++i) {
            testDataArray[i].a = 0;
            testDataArray[i].b = 0;
            results[i].a = 0;
            results[i].b = 0;
        }
        start = false;
    }

    int actor1(TestData t) {
        t.a = 1;
        return t.b;
    }

    int actor2(TestData t) {
        t.b = 1;
        return t.a;
    }

    class Runner1 extends Thread {
        public void run() {
            do {} while (!start);
            for (int i = 0; i < ITERATIONS; ++i) {
                results[i].a = actor1(testDataArray[i]);
            }
        }
    }

    class Runner2 extends Thread {
        public void run() {
            do {} while (!start);
            for (int i = 0; i < ITERATIONS; ++i) {
                results[i].b = actor2(testDataArray[i]);
            }
        }
    }

    void testRunner() {
        Thread thread1 = new Runner1();
        Thread thread2 = new Runner2();
        thread1.start();
        thread2.start();
        do {} while (!thread1.isAlive());
        do {} while (!thread2.isAlive());
        start = true;
        Thread.yield();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
            System.exit(1);
        }
    }

    boolean printResult() {
        int[] count = new int[4];
        for (int i = 0; i < ITERATIONS; ++i) {
            int event_kind = (results[i].a << 1) + results[i].b;
            ++count[event_kind];
        }
        if (count[0] == 0 && count[3] == 0) {
            System.out.println("[not interesting]");
            return false; 
        }
        String error = (count[0] == 0) ? " ok" : " disallowed!";
        System.out.println("[0,0] " + count[0] + error);
        System.out.println("[0,1] " + count[1]);
        System.out.println("[1,0] " + count[2]);
        System.out.println("[1,1] " + count[3]);
        return (count[0] != 0);
    }

    public static void main(String args[]) {
        DekkerTest test = new DekkerTest();
        final int runs = 30;
        int failed = 0;
        for (int c = 0; c < runs; ++c) {
            test.testRunner();
            if (test.printResult()) {
                failed++;
            }
            test.reset();
        }
        if (failed > 0) {
            throw new InternalError("FAILED. Got " + failed + " failed ITERATIONS");
        }
        System.out.println("PASSED.");
    }

}
