



public class CastToTypeVarTest<X, Y extends X> {
    void foo(Y y) {
        X x = (X)y;
    }
}
