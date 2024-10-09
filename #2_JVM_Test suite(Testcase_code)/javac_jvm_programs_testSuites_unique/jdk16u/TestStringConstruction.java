



package compiler.intrinsics.string;

public class TestStringConstruction {

    public static void main(String[] args) {
        char[] chars = new char[42];
        for (int i = 0; i < 10_000; ++i) {
            test(chars);
        }
    }

    private static String test(char[] chars) {
        try {
            
            
            
            
            
            return new String(chars, -1 , 42);
        } catch (Exception e) {
            return "";
        }
    }
}

