


package compiler.loopopts.superword;

public class CoLocatePack {

    public static long lFld = 10;
    public static float fFld = 11.2f;
    public int iFld = 12;

    public void test() {
        int iArr[] = new int[200];
        float fArr[] = new float[200];

        
        for (int i = 5; i < 169; i++) {
            fArr[i + 1] += ((long)(fFld) | 1); 
            iFld += lFld;
            fArr[i - 1] -= 20; 
            fFld += i;
            fArr[i + 1] -= -117; 

            int j = 10;
            do {
            } while (--j > 0);

            iArr[i] += 11;
        }
    }

    public static void main(String[] strArr) {
        CoLocatePack _instance = new CoLocatePack();
        for (int i = 0; i < 1000; i++ ) {
            _instance.test();
        }
    }
}
