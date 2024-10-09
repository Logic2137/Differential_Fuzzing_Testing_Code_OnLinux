
package compiler.debug;

public class TraceIterativeGVN {

    public static void main(String[] args) {
        for (int i = 0; i < 100_000; i++) {
            Byte.valueOf((byte) 0);
        }
        System.out.println("TEST PASSED");
    }
}
