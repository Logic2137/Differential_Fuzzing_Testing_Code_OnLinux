
package compiler.interpreter;

public class Test6833129 {

    public static void init(int[] src) {
        for (int i = 0; i < src.length; i++) {
            src[i] = i;
        }
    }

    public static void clone_and_verify(int[] src) {
        for (int i = 0; i < src.length; i++) {
            int[] src_clone = src.clone();
            if (src[i] != src_clone[i]) {
                System.out.println("Error: allocated but not copied: ");
                for (int j = 0; j < src_clone.length; j++) System.out.print(" " + src_clone[j]);
                System.out.println();
                System.exit(97);
            }
        }
    }

    public static void test() {
        int[] src = new int[34];
        init(src);
        clone_and_verify(src);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            test();
        }
    }
}
