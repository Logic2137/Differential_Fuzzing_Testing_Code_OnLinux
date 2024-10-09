
package vm.compiler.coverage.parentheses.share.generation;

import java.util.Random;



public class ParenthesesGenerator {
    private static Random random = new Random();

    public static String generate(int size) {
        if (size == 0) {
            return "";
        }
        if (random.nextBoolean()) {
            return "(" + generate(size - 1) + ")";
        } else {
            int splitPoint = random.nextInt(size + 1);
            return generate(splitPoint) + generate(size - splitPoint);
        }
    }
}
