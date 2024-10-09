import java.util.Arrays;

public class TestStringObjectInitialization {

    String myString;

    public static void main(String[] args) throws Exception {
        TestStringObjectInitialization t = new TestStringObjectInitialization();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
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
}

class Runner implements Runnable {

    private TestStringObjectInitialization test;

    public Runner(TestStringObjectInitialization t) {
        test = t;
    }

    public void run() {
        String[] array = { "a", "b", "c" };
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100_000; ++i) {
            test.run("a", array);
            test.reset();
        }
    }
}
