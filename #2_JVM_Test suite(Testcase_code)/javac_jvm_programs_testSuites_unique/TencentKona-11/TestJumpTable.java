



package compiler.c2;

public class TestJumpTable {

    public static int test0() {
        int res = 0;
        for (int i = 10; i < 50; ++i) {
            switch (i * 5) {
                case 15:
                case 25:
                case 40:
                case 101:
                    return 42;
                case 45:
                case 51:
                case 60:
                    res++;
                    break;
            }
        }
        return res;
    }

    static int field;

    
    public static void test1() {
        int i4, i5 = 99, i6, i9 = 89;
        for (i4 = 12; i4 < 365; i4++) {
            for (i6 = 5; i6 > 1; i6--) {
                switch ((i6 * 5) + 11) {
                case 13:
                case 19:
                case 26:
                case 31:
                case 35:
                case 41:
                case 43:
                case 61:
                case 71:
                case 83:
                case 314:
                    i9 = i5;
                    break;
                }
            }
        }
    }

    
    
    public static void test2() {
        for (int i = 5; i > -10; i--) {
            switch (i) {
            case 0:
            case 4:
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
            case 100:
                field = 42;
                break;
            }
        }
    }

    
    
    public static void test3() {
        for (int i = 5; i > -20; i -= 5) {
            switch (i) {
            case 0:
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
            case 60:
            case 100:
                field = 42;
                break;
            }
        }
    }

    
    
    public static void test4() {
        int local = 0;
        for (int i = 5; i > -20; i -= 5) {
            switch (i) {
            case 0:
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
            case 100:
                local = 42;
                break;
            }
        }
    }

    
    
    public static void test5() {
        int local;
        for (int i = 25; i > 0; i -= 5) {
            switch (i) {
            case 20:
            case 30:
            case 40:
            case 50:
            case 60:
            case 70:
            case 300:
                local = 42;
                break;
            }
        }
    }

    
    
    public static void test6() {
        int local;
        for (int i = 25; i > 0; i -= 5) {
            switch (i + 10) {
            case 30:
            case 40:
            case 50:
            case 60:
            case 70:
            case 80:
            case 300:
                local = 42;
                break;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50_000; ++i) {
            test0();
            test1();
            test2();
            test3();
            test4();
            test5();
            test6();
        }
    }
}
