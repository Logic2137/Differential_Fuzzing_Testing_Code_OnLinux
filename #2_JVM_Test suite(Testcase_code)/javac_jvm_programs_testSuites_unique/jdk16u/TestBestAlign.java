



package compiler.loopopts.superword;

public class TestBestAlign {

    static final int initVal = -1;
    static int intArray [];
    static boolean boolArray[];
    static int limit;
    static public void clear() {
        for (int i = 0; i < limit; i++) {
            boolArray[1] = true;
            intArray[i] = initVal;
            boolArray[2] = true;
        }
    }

    public static void main(String argv[]) throws Exception {
        limit = 64;
        boolArray = new boolean[8];
        intArray = new int[limit + 4];
        for (int i = 0; i < 10000000; ++i) {
            if(i % 1000000 == 0)
                System.out.println(i);
            clear();
        }
    }
}
