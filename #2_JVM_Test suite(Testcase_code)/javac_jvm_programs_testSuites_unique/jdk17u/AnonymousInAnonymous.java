public class AnonymousInAnonymous {

    static void s(I1 i) {
    }

    static {
        s(new I1() {

            public I2 get() {
                return new I2() {
                };
            }
        });
    }

    public static interface I1 {

        public static class I2 {
        }

        public I2 get();
    }
}
