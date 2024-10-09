



package compiler.loopopts.superword;

public class SuperWordIntermediateUse {

    private int iFld;
    private int[] iArr = new int[1024];

    public void test() {
        int local = 4;

        
        for (int i = 0; i < 1024; i++) {
            iFld = -85;
            iFld = iFld + local;
            local = local * iArr[i];
            iArr[i] = 3; 
        }
    }

    public static void main(String[] strArr) {
        SuperWordIntermediateUse instance = new SuperWordIntermediateUse();
        for (int i = 0; i < 1000; i++) {
            instance.test();
        }
    }
}
