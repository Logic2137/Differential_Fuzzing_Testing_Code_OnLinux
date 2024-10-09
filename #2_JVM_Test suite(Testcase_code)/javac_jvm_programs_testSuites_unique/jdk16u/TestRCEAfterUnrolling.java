



public class TestRCEAfterUnrolling {

     public static int iFld = 0;
     public static short sFld = 1;

     public static void main(String[] strArr) {
         test();
     }

     public static int test() {
         int x = 11;
         int y = 0;
         int j = 0;
         int iArr[] = new int[400];

         init(iArr);

         for (int i = 0; i < 2; i++) {
             doNothing();
             for (j = 10; j > 1; j -= 2) {
                 sFld += (short)j;
                 iArr = iArr;
                 y += (j * 3);
                 x = (iArr[j - 1]/ x);
                 x = sFld;
             }
             int k = 1;
             while (++k < 8) {
                 iFld += x;
             }
         }
         return Float.floatToIntBits(654) + x + j + y;
     }

     
     public static void doNothing() {
     }

     
     public static void init(int[] a) {
         for (int j = 0; j < a.length; j++) {
             a[j] = 0;
         }
     }
}


