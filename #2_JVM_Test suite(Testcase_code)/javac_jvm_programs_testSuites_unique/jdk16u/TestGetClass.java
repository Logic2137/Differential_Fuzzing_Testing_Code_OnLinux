



package compiler.escapeAnalysis;

public class TestGetClass {
    static Object obj = new Object();

    public static boolean test() {
        if (obj.getClass() == Object.class) {
            synchronized (obj) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        if (!test()) {
            throw new RuntimeException("Test failed");
        }
    }
}
