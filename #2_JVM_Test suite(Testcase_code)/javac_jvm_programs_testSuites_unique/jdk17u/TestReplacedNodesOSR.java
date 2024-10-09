public class TestReplacedNodesOSR {

    static Object dummy;

    static interface I {
    }

    static class A implements I {
    }

    static final class MyException extends Exception {
    }

    static final A obj = new A();

    static I static_field() {
        return obj;
    }

    static void test(int v, MyException e) {
        int i = 0;
        for (; ; ) {
            if (i == 1000) {
                break;
            }
            try {
                if ((i % 2) == 0) {
                    int j = 0;
                    for (; ; ) {
                        j++;
                        if (i + j != v) {
                            if (j == 1000) {
                                break;
                            }
                        } else {
                            A a = (A) static_field();
                            throw e;
                        }
                    }
                }
            } catch (MyException ex) {
            }
            i++;
            dummy = static_field();
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            test(1100, new MyException());
        }
    }
}
