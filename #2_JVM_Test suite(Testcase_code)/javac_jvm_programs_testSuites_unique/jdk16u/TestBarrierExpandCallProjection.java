



public class TestBarrierExpandCallProjection {
    static TestBarrierExpandCallProjection test = new TestBarrierExpandCallProjection();
    private int field;

    public static void main(String[] args) {
        test();
    }

    private static int test() {
        int v = 0;
        for (int i = 0; i < 100_000; i++) {
            v += test.field;
        }
        return v;
    }
}
