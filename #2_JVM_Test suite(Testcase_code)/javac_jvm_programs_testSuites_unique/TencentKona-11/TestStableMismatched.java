



package compiler.stable;

public class TestStableMismatched {
    public static void main(String args[]) {
        test();
    }

    public static void test() {
        String text = "abcdefg";
        
        char returned = text.charAt(6);
        if (returned != 'g') {
            throw new RuntimeException("failed: charAt(6) returned '" + returned + "' instead of 'g'");
        }
    }
}

