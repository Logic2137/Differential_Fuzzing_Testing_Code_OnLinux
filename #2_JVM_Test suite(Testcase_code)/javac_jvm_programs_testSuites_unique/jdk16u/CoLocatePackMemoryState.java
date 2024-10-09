




package compiler.loopopts.superword;

public class CoLocatePackMemoryState {

    public static final int N = 64;
    public static byte byFld;
    public static int iArr[] = new int[N+1];

    public static void test() {
        
        
        for (int i = 0; i < N; ++i) {
            iArr[i+1] = i;
            iArr[i] -= 15;
            byFld += 35;
        }
    }

    public static void main(String[] strArr) {
        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < N; j++) {
                iArr[j] = 0;
            }
            test();

            if (iArr[0] != -15) {
                throw new RuntimeException("iArr[0] must be -15 but was " + iArr[0]);
            }
            for (int j = 1; j < N; j++) {
                if (iArr[j] != (j-16)) {
                    throw new RuntimeException("iArr[" + j + "] must be " + (j-16) + " but was " + iArr[j]);
                }
            }
        }
    }
}
