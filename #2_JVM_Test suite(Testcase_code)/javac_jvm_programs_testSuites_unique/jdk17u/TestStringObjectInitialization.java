
package compiler.stringopts;

import java.util.Arrays;

public class TestStringObjectInitialization {

    String myString;

    public static void main(String[] args) throws Exception {
        TestStringObjectInitialization t = new TestStringObjectInitialization();
        for (int i = 0; i < 100; ++i) {
            (new Thread(new Runner(t))).start();
        }
        Thread last = new Thread(new Runner(t));
        last.start();
        last.join();
    }

    private void add(String message) {
        myString += message;
    }

    public void run(String s, String[] sArray) {
        add(s + Arrays.toString(sArray) + " const ");
    }

    public void reset() {
        myString = "";
    }

    private static class Runner implements Runnable {

        private TestStringObjectInitialization test;

        public Runner(TestStringObjectInitialization t) {
            test = t;
        }

        public void run() {
            String[] array = { "a", "b", "c" };
            for (int i = 0; i < 100_000; ++i) {
                test.run("a", array);
                test.reset();
            }
        }
    }
}
