



package compiler.intrinsics;

public class Test8215792 {

    private static final int ITERATIONS = 10000;
    private static final String pattern = "01234567890123456789";

    public static void main(String[] args) {

        
        for (int iter = 0; iter < ITERATIONS; iter++) {
            StringBuilder str1 = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789");
            StringBuilder str2 = new StringBuilder("\u4f60\u598dCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789");

            for (int i = 0; i < 20; i++) {
                
                str1.setLength(str1.length() - 1);
                str2.setLength(str2.length() - 1);
                
                if (str1.indexOf(pattern) != -1 || str2.indexOf(pattern) != -1) {
                    System.out.println("FAILED");
                    System.exit(1);
                }
            }
        }
        System.out.println("PASSED");
    }
}

