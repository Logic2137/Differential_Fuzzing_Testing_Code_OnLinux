



package compiler.intrinsics;

public class Test8005419 {
    public static int SIZE = 64;

    public static void main(String[] args) {
        char[] a = new char[SIZE];
        char[] b = new char[SIZE];

        for (int i = 16; i < SIZE; i++) {
          a[i] = (char)i;
          b[i] = (char)i;
        }
        String s1 = new String(a);
        String s2 = new String(b);

        
        boolean failed = false;
        int result = 0;
        for (int i = 0; i < 10000; i++) {
          result += test(s1, s2);
        }
        for (int i = 0; i < 10000; i++) {
          result += test(s1, s2);
        }
        for (int i = 0; i < 10000; i++) {
          result += test(s1, s2);
        }
        if (result != 0) failed = true;

        System.out.println("Start testing");
        
        result = test(s1, s1);
        if (result != 0) {
          failed = true;
          System.out.println("Failed same: result = " + result + ", expected 0");
        }
        
        for (int i = 1; i <= SIZE; i++) {
          s1 = new String(a, 0, i);
          s2 = new String(b, 0, i);
          result = test(s1, s2);
          if (result != 0) {
            failed = true;
            System.out.println("Failed equals s1[" + i + "], s2[" + i + "]: result = " + result + ", expected 0");
          }
        }
        
        for (int i = 1; i <= SIZE; i++) {
          s1 = new String(a, 0, i);
          for (int j = 1; j <= SIZE; j++) {
            s2 = new String(b, 0, j);
            result = test(s1, s2);
            if (result != (i-j)) {
              failed = true;
              System.out.println("Failed diff size s1[" + i + "], s2[" + j + "]: result = " + result + ", expected " + (i-j));
            }
          }
        }
        
        for (int i = 1; i <= SIZE; i++) {
          s1 = new String(a, 0, i);
          for (int j = 0; j < i; j++) {
            b[j] -= 3; 
            s2 = new String(b, 0, i);
            result = test(s1, s2);
            int chdiff = a[j] - b[j];
            if (result != chdiff) {
              failed = true;
              System.out.println("Failed diff char s1[" + i + "], s2[" + i + "]: result = " + result + ", expected " + chdiff);
            }
            result = test(s2, s1);
            chdiff = b[j] - a[j];
            if (result != chdiff) {
              failed = true;
              System.out.println("Failed diff char s2[" + i + "], s1[" + i + "]: result = " + result + ", expected " + chdiff);
            }
            b[j] += 3; 
          }
        }
        if (failed) {
          System.out.println("FAILED");
          System.exit(97);
        }
        System.out.println("PASSED");
    }

    private static int test(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
