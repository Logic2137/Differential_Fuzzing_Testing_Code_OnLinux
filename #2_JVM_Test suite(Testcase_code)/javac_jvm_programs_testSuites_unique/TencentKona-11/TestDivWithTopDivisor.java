



package compiler.loopopts;

public class TestDivWithTopDivisor {
    static boolean bFld;

    static int test(int d, long e, long f) {
        float g = 1;
        int a, b = 4, c = 4;
        int iArr[] = new int[400];
        init(iArr, 8);
        if (bFld) {
        } else if (bFld) {
            if (bFld) {
                if (bFld) {
                    for (a = 7; a > 1; --a) {
                        if (bFld) {
                            try {
                                c = b / a;
                                b = 9 / a;
                            } catch (ArithmeticException k) {
                            }
                        }
                    }
                }
                g = 0;
            }
        } else {
            iArr[d] <<= b;
        }
        long l = f + c + checkSum(iArr);
        return (int) l;
    }

    public static void init(int[] a, int seed) {
        for (int j = 0; j < a.length; j++) {
            a[j] = (j % 2 == 0) ? seed + j : seed - j;
        }
    }

    public static long checkSum(int[] a) {
        long sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += (a[j] / (j + 1) + a[j] % (j + 1));
        }
        return sum;
    }

    public static void main(String[] s) {
        for (int i = 0; i < 10; i++) {
            test(3, 0, 0);
        }
    }
}

