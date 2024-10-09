public class T8058199 {

    final static String SYNTHETIC_CAST_TYPE = "[Ljava.lang.String;";

    @SuppressWarnings("unchecked")
    <Z> Z[] makeArr(Z z) {
        return (Z[]) new Object[1];
    }

    <U> void check(U u) {
    }

    void testMethod() {
        test(() -> check(makeArr("")));
    }

    void testNewDiamond() {
        class Check<X> {

            Check(X x) {
            }
        }
        test(() -> new Check<>(makeArr("")));
    }

    void testNewGeneric() {
        class Check {

            <Z> Check(Z z) {
            }
        }
        test(() -> new Check(makeArr("")));
    }

    private void test(Runnable r) {
        try {
            r.run();
            throw new AssertionError("Missing synthetic cast");
        } catch (ClassCastException cce) {
            if (!cce.getMessage().contains(SYNTHETIC_CAST_TYPE)) {
                throw new AssertionError("Bad type in synthetic cast", cce);
            }
        }
    }

    public static void main(String[] args) {
        T8058199 test = new T8058199();
        test.testMethod();
        test.testNewDiamond();
        test.testNewGeneric();
    }
}
