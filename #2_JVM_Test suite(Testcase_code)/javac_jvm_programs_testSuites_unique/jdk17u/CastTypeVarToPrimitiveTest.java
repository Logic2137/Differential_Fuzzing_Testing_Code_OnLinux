public class CastTypeVarToPrimitiveTest<T> {

    void foo(T valIn) {
        double val = (double) valIn;
    }
}
