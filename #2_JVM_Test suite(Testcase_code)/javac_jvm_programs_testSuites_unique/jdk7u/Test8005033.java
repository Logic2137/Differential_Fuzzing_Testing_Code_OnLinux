



public class Test8005033 {
    public static int MINUS_ONE = -1;

    public static void main(String[] args) {
        System.out.println("EXECUTING test.");
        Integer.bitCount(1);   
        int expectedBitCount = 0;
        int calculatedBitCount = testBitCount();
        if (expectedBitCount != calculatedBitCount) {
            throw new InternalError("got " + calculatedBitCount + " but expected " + expectedBitCount);
        }
        System.out.println("SUCCESSFULLY passed test.");
    }

    
    private static int testBitCount() {
        return Integer.bitCount(MINUS_ONE+1);   
    }
}
