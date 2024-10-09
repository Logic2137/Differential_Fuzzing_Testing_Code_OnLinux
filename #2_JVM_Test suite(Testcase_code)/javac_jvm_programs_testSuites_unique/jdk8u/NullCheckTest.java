public class NullCheckTest {

    static class A {

        int f;

        public final void inlined(A a) {
            B b = ((B) a);
        }
    }

    static class B extends A {
    }

    private static void test(A a1, A a2) {
        a1.inlined(a2);
        int x = a1.f;
    }

    public static void main(String[] args) {
        new B();
        try {
            test(null, new A());
            throw new InternalError("FAILURE: no exception");
        } catch (NullPointerException ex) {
            System.out.println("CORRECT: NullPointerException");
        } catch (ClassCastException ex) {
            System.out.println("FAILURE: ClassCastException");
            throw ex;
        }
    }
}
