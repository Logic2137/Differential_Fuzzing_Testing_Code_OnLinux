



package compiler.runtime;

public class Test6778657 {
    public static void check_f2i(int expect) {
        float check = expect;
        check *= 2;
        int actual = (int) check;
        if (actual != expect) {
            throw new RuntimeException("expecting " + expect + ", got " + actual);
        }
    }

    public static void check_f2l(long expect) {
        float check = expect;
        check *= 2;
        long actual = (long) check;
        if (actual != expect) {
            throw new RuntimeException("expecting " + expect + ", got " + actual);
        }
    }

    public static void check_d2i(int expect) {
        double check = expect;
        check *= 2;
        int actual = (int) check;
        if (actual != expect) {
            throw new RuntimeException("expecting " + expect + ", got " + actual);
        }
    }

    public static void check_d2l(long expect) {
        double check = expect;
        check *= 2;
        long actual = (long) check;
        if (actual != expect) {
            throw new RuntimeException("expecting " + expect + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        check_f2i(Integer.MAX_VALUE);
        check_f2i(Integer.MIN_VALUE);
        check_f2l(Long.MAX_VALUE);
        check_f2l(Long.MIN_VALUE);
        check_d2i(Integer.MAX_VALUE);
        check_d2i(Integer.MIN_VALUE);
        check_d2l(Long.MAX_VALUE);
        check_d2l(Long.MIN_VALUE);
    }
}

