









import java.util.function.*;

class BoundUnboundMethRefSearch2 {
    interface SAM <T> {
        boolean test(T n, T m);
    }

    static <T> boolean foo(T x, T y) {
        return false;
    }

    void bar() {
        SAM <Integer> mRef = BoundUnboundMethRefSearch2::<Integer>foo;
    }

}
