
package compiler.conversions;

public class TestChainOfIntAddsToLongConversion {

    public static void main(String[] args) {
        long out = 0;
        for (int i = 0; i < 2; i++) {
            int foo = i;
            for (int j = 0; j < 17; j++) {
                foo = foo + foo;
            }
            out = foo;
        }
        System.out.println(out);
    }
}
