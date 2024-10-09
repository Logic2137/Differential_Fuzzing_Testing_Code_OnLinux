
package compiler.arguments;

public class TestStressReflectiveCode {

    public static void main(String[] args) {
        VALUES.clone();
    }

    private static final int[] VALUES = new int[] { 3, 4, 5 };
}
