public class T8198502 {

    public static void main(String... args) {
        new T8198502().run();
    }

    private void run() {
        Base b = new Base() {

            public void run() {
                test(this);
            }
        };
        b.run();
    }

    private static void test(T8198502 t) {
        throw new AssertionError("Should not be invoked!");
    }

    private static void test(Base b) {
    }

    public static interface Base {

        void run();
    }
}
