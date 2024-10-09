


package compiler.c2;

public class TestRotateNegativeEvenValues {

    public static void run() {
        test(1 << 31); 
        test(1L << 63); 
        test(-1 << 10); 
        test(-1L << 10); 
        test(-1 << 20); 
        test(-1L << 20); 
        test(-2); 
        test(-2L); 
        test(-3546); 
        test(-3546L); 
    }

    
    public static void test(int negativeEvenNumber) {
        for (int i = 1; i <= 1; i++) {
            int leftShift = negativeEvenNumber << -i;
            int rightShift = negativeEvenNumber >>> i;
            if ((leftShift | rightShift) != (rightShift | leftShift)) {
                int or1 = leftShift | rightShift;
                int or2 = rightShift | leftShift;
                throw new RuntimeException("Or operations are not equal:" + " " + or1 + " vs. "+ or2
                                           + " - leftShift: " + leftShift + ", rightShift: " + rightShift);
            }
        }
    }

    
    public static void test(long negativeEvenNumber) {
        for (int i = 1; i <= 1; i++) {
            long leftShift = negativeEvenNumber << -i;
            long rightShift = negativeEvenNumber >>> i;
            if ((leftShift | rightShift) != (rightShift | leftShift)) {
                long or1 = leftShift | rightShift;
                long or2 = rightShift | leftShift;
                throw new RuntimeException("Or operations are not equal:" + " " + or1 + " vs. "+ or2
                                           + " - leftShift: " + leftShift + ", rightShift: " + rightShift);
            }
        }
    }

    public static void main(String argv[]) {
        run();
    }
}
