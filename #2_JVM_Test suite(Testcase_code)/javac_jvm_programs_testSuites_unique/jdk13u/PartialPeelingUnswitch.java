
package compiler.loopopts;

public class PartialPeelingUnswitch {

    public static int iFld;

    public static int x = 42;

    public static int y = 31;

    public static int z = 22;

    public static int[] iArr = new int[10];

    public int test() {
        iFld = 13;
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                switch((i * 5) + 102) {
                    case 120:
                        break;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        iFld >>= 1;
                }
            }
        }
        return iFld;
    }

    public int test2() {
        iFld = 13;
        int k = 0;
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                x = y + iFld;
                y = iArr[5];
                k = 6;
                iArr[5] = 5;
                iArr[6] += 23;
                iArr[7] = iArr[8] + iArr[6];
                iArr[j] = 34;
                switch((i * 5) + 102) {
                    case 120:
                        break;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        iFld >>= 1;
                }
            }
        }
        return iFld + k;
    }

    public int test3() {
        iFld = 13;
        if (z < 34) {
            z = 34;
        }
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                iArr[5] = 8;
                x = iArr[6];
                y = x;
                for (int k = 50; k < 51; k++) {
                    x = iArr[7];
                }
                switch((i * 5) + 102) {
                    case 120:
                        return iFld;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        if (iFld == -7) {
                            return iFld;
                        }
                        z = iArr[5];
                        iFld >>= 1;
                }
            }
            iArr[5] = 34;
            dontInline(iArr[5]);
        }
        return iFld;
    }

    public int test4() {
        iFld = 13;
        if (z < 34) {
            z = 34;
        }
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                iArr[5] = 8;
                x = iArr[6];
                y = x;
                for (int k = 50; k < 51; k++) {
                    x = iArr[7];
                }
                switch((i * 5) + 102) {
                    case 120:
                        return iFld;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        if (iFld == -7) {
                            return iFld;
                        }
                        z = iArr[5];
                        iFld >>= 1;
                }
            }
            iArr[5] = 34;
        }
        return iFld;
    }

    public int test5() {
        iFld = 13;
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                iArr[5] = 8;
                x = iArr[6];
                y = x;
                for (int k = 50; k < 51; k++) {
                    x = iArr[7];
                }
                switch((i * 5) + 102) {
                    case 120:
                        return iFld;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        iFld >>= 1;
                }
            }
        }
        return iFld;
    }

    public int test6() {
        iFld = 13;
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                iArr[5] = 8;
                x = iArr[6];
                y = x;
                switch((i * 5) + 102) {
                    case 120:
                        return iFld;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        iFld >>= 1;
                }
            }
        }
        return iFld;
    }

    public int test7() {
        iFld = 13;
        for (int i = 0; i < 8; i++) {
            int j = 10;
            while (--j > 0) {
                iFld += -7;
                iArr[5] = 8;
                switch((i * 5) + 102) {
                    case 120:
                        return iFld;
                    case 103:
                        break;
                    case 116:
                        break;
                    default:
                        iFld >>= 1;
                }
            }
        }
        return iFld;
    }

    public static void main(String[] strArr) {
        PartialPeelingUnswitch _instance = new PartialPeelingUnswitch();
        for (int i = 0; i < 2000; i++) {
            int result = _instance.test();
            if (result != -7) {
                throw new RuntimeException("Result should always be -7 but was " + result);
            }
        }
        for (int i = 0; i < 2000; i++) {
            int result = _instance.test2();
            check(-1, result);
            check(-7, iFld);
            check(-9, x);
            check(5, y);
            check(5, iArr[5]);
            check(149, iArr[6]);
            check(183, iArr[7]);
            for (int j = 0; j < 10; j++) {
                iArr[j] = 0;
            }
            x = 42;
            y = 31;
        }
        for (int i = 0; i < 2000; i++) {
            _instance.test3();
            _instance.test4();
            _instance.test5();
            _instance.test6();
            _instance.test7();
        }
        for (int i = 0; i < 2000; i++) {
            if (i % 2 == 0) {
                z = 23;
            }
            _instance.test3();
            _instance.test4();
        }
    }

    public static void check(int expected, int actual) {
        if (expected != actual) {
            throw new RuntimeException("Wrong result, expected: " + expected + ", actual: " + actual);
        }
    }

    public void dontInline(int i) {
    }
}
