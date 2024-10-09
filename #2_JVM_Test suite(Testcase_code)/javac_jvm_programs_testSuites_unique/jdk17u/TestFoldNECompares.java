
package compiler.c2;

public class TestFoldNECompares {

    public static int getNarrowInt(boolean b, int lo, int hi) {
        return b ? lo : hi;
    }

    public static void test1(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i != 42) {
            if (i <= 42) {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test2(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i != 42) {
            if (i > 42) {
            } else {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test3(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i == 42) {
        } else {
            if (i <= 42) {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test4(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i == 42) {
        } else {
            if (i > 42) {
            } else {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test5(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i != 142) {
            if (i >= 142) {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test6(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i != 142) {
            if (i < 142) {
            } else {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test7(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i == 142) {
        } else {
            if (i >= 142) {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void test8(boolean b) {
        int i = getNarrowInt(b, 42, 142);
        if (i == 142) {
        } else {
            if (i < 142) {
            } else {
                throw new RuntimeException("Should not reach here");
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100_000; ++i) {
            boolean b = ((i % 2) == 0);
            test1(b);
            test2(b);
            test3(b);
            test4(b);
            test5(b);
            test6(b);
            test7(b);
            test8(b);
        }
    }
}
