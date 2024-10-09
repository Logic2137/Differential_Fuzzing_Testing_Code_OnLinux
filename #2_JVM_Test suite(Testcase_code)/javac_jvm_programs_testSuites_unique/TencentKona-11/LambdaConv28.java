

class LambdaConv28 {

    public void test(A a) {
        test(()-> {
            return new I() {
                public <T> void t() {
                }
            };
        });
        test(new A() {
            public I get() {
                return null;
            }
        });
    }

    public interface I {
        public <T> void t();
    }

    public interface A {
        public I get();
    }

}
