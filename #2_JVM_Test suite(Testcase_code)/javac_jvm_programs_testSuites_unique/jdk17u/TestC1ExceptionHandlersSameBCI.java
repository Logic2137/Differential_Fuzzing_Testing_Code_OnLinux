public class TestC1ExceptionHandlersSameBCI {

    static class Ex1 extends Exception {
    }

    static class Ex2 extends Exception {
    }

    static void not_inline1() throws Ex1, Ex2 {
    }

    static void not_inline2(int v) {
    }

    static void test1() throws Ex1, Ex2 {
        int i = 0;
        try {
            not_inline1();
            i = 1;
            not_inline1();
        } catch (Ex1 | Ex2 ex) {
            not_inline2(i);
        }
    }

    static void test2() {
        int i = 0;
        try {
            test1();
            i = 1;
            test1();
        } catch (Ex1 | Ex2 ex) {
            not_inline2(i);
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 5000; i++) {
            test2();
        }
    }
}
