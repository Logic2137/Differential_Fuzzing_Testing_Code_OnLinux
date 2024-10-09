public class InferenceVariableInstantiatedUnexpectedlyTest {

    interface Iface<A1 extends A2, A2> {

        String m(A1 t);
    }

    public void run() {
        Iface<? super Integer, Number> i = (Integer a) -> a.toString();
    }
}
