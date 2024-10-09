


package compiler.loopopts;

public class TestUnswitchCloneSkeletonPredicates {

    static int x = 0;
    static int y = 20;
    static int intArr[] = new int[21000];
    static int idx = 0;
    static boolean bFld = true;
    static int iFld = 20;
    static int iFld2 = 0 ;
    static int iArrFld[] = new int[50];
    static float fArrFld[] = new float[50];


    
    
    public static int test1() {
      int i = 0;
      while (i < 100) {
          int j = 0;
          
          while (j < 10000)  {
              if (x == 100) { 
                  y = 34;
              }

              intArr[idx] = 34;
              intArr[2*j + 35] = 45;

              if (x == 100) { 
                  y = 35;
                  break;
              }
              if (j == 9800) { 
                  return 2;
              }
              j++;
          }
          i++;
          intArr[i] = 45;
      }
      return y;
    }

    
    public static int test2() {
      int i = 0;
      while (i < 100) {
          int j = 0;
          while (j < 10000)  {
              if (x == 100) {
                  y = 34;
              }

              intArr[2*j + 35] = 45;

              if (x == 100) {
                  y = 35;
                  break;
              }
              if (j == 9800) {
                  return 2;
              }
              j++;
          }
          i++;
          intArr[i] = 45;
      }
      return y;
    }

    
    public static int test3() {
      int i = 0;
      while (i < 100) {
          int j = 0;
          while (j < 10000)  {
              if (x == 100) {
                  y = 34;
              }

              intArr[idx] = 34;
              intArr[2*j + 35] = 45;

              if (x == 100) {
                  y = 35;
                  break;
              }
              if (j == 9800) {
                  return 2;
              }
              j++;
          }
          i++;
      }
      return y;
}

    
    
    public static void test4() {
        int unused = 500; 
        boolean b = true;
        int i = 1;
        while (++i < 35) {
            iArrFld[i] = 6;
            switch (iFld2) {
            case 40:
                if (b) {
                    continue;
                }
                b = false;
                break;
            }
        }
    }

    
    
    public static void test5() {
        int j = 50;
        int i = 1;
        while (++i < 40) {
            j = 5;
            do {
                fArrFld[i] = 46;
                iFld = 5;
                if (bFld) break;
            } while (++j < 5);
            j = 2;
            do {
                try {
                    iFld = 56;
                } catch (ArithmeticException a_e) {}
                if (bFld) break;
            } while (++j < 2);
        }
    }

    public static void main(String[] strArr) {
        for (int i = 0; i < 5000; i++) {
            test1();
            test2();
            test3();
            x++;
            x = x % 106;
        }
        test4();
        test5();
    }
}
