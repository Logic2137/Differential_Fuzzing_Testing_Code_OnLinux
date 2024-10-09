



public class Test6443505 {

    public static void main(String[] args) throws InterruptedException {
        test(Integer.MIN_VALUE, 0);
        test(0, Integer.MIN_VALUE);
        test(Integer.MIN_VALUE, -1);
        test(-1, Integer.MIN_VALUE);
        test(Integer.MIN_VALUE, 1);
        test(1, Integer.MIN_VALUE);

        test(Integer.MAX_VALUE, 0);
        test(0, Integer.MAX_VALUE);
        test(Integer.MAX_VALUE, -1);
        test(-1, Integer.MAX_VALUE);
        test(Integer.MAX_VALUE, 1);
        test(1, Integer.MAX_VALUE);

        test(Integer.MIN_VALUE, Integer.MAX_VALUE);
        test(Integer.MAX_VALUE, Integer.MIN_VALUE);

        test(1, -1);
        test(1, 0);
        test(1, 1);
        test(-1, -1);
        test(-1, 0);
        test(-1, 1);
        test(0, -1);
        test(0, 0);
        test(0, 1);
    }

    public static void test(int a, int b) throws InterruptedException {
        int C = compiled(4, a, b);
        int I = interpreted(4, a, b);
        if (C != I) {
            System.err.println("#1 C = " + C + ", I = " + I);
            System.err.println("#1 C != I, FAIL");
            System.exit(97);
        }

        C = compiled(a, b, q, 4);
        I = interpreted(a, b, q, 4);
        if (C != I) {
            System.err.println("#2 C = " + C + ", I = " + I);
            System.err.println("#2 C != I, FAIL");
            System.exit(97);
        }

    }

    static int q = 4;

    
    
    static int compiled(int p, int x, int y) {
        return (x < y) ? q + (x - y) : (x - y);
    }

    
    static int interpreted(int p, int x, int y) {
        return (x < y) ? q + (x - y) : (x - y);
    }

    
    
    static int compiled(int x, int y, int q, int p) {
        return (x < y) ? p + q : q;
    }

    
    static int interpreted(int x, int y, int q, int p) {
        return (x < y) ? p + q : q;
    }

}
