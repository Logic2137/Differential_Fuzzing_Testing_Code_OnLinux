class Exception1 extends Exception {
}

class Exception2 extends Exception {
}

public class CatchInlineExceptions {

    private static int counter0;

    private static int counter1;

    private static int counter2;

    private static int counter;

    static void foo(int i) throws Exception {
        if ((i & 1023) == 2) {
            counter0++;
            throw new Exception2();
        }
    }

    static void test(int i) throws Exception {
        try {
            foo(i);
        } catch (Exception e) {
            if (e instanceof Exception1) {
                counter1++;
            } else if (e instanceof Exception2) {
                counter2++;
            }
            counter++;
            throw e;
        }
    }

    public static void main(String[] args) throws Throwable {
        for (int i = 0; i < 15000; i++) {
            try {
                test(i);
            } catch (Exception e) {
            }
        }
        if (counter1 != 0) {
            throw new RuntimeException("Failed: counter1(" + counter1 + ") != 0");
        }
        if (counter2 != counter0) {
            throw new RuntimeException("Failed: counter2(" + counter2 + ") != counter0(" + counter0 + ")");
        }
        if (counter2 != counter) {
            throw new RuntimeException("Failed: counter2(" + counter2 + ") != counter(" + counter + ")");
        }
        System.out.println("TEST PASSED");
    }
}
