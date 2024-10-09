



import java.util.Arrays;
import java.util.List;

public class InvalidReceiverTypeTest {

    static abstract class A {}

    interface B {
        boolean g();
    }

    static class C extends A implements B {
        public boolean g() {
            return true;
        }
    }

    static class D<R extends A & B> {
        public long f(List<? extends R> xs) {
            return xs.stream().filter(B::g).count();
        }
    }

    public static void main(String[] args) {
        long count = new D<C>().f(Arrays.asList(new C()));
        System.err.println(count);
    }
}
