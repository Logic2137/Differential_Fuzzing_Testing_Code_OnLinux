



public class StoreMovedBeforeInfiniteLoop {
    public static void main(String[] args) {
        field = -1;
        test(new Object());
    }

    static int field;

    static int constant() {
        return 65;
    }

    private static int test(Object o) {
        do {
            if (field <= 0) {
                return -109;
            }
            do {
                field = 4;
            } while (constant() >= 0);
        } while (o == null);
        return -109;
    }
}
