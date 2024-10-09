public class TestIntArraySubTypeOfCloneableDoesnotFold {

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test();
        }
    }

    private static boolean test() {
        return int[].class.isAssignableFrom(Cloneable.class);
    }
}
