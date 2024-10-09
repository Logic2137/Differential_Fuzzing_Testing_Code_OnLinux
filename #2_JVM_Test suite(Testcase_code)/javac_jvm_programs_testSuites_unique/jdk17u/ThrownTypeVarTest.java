import java.io.*;

public class ThrownTypeVarTest {

    void repro() throws IOException {
        when(f(any()));
    }

    interface MyInt<T1, E1 extends Exception> {
    }

    <T2, E2 extends Exception> T2 f(MyInt<T2, E2> g) throws IOException, E2 {
        return null;
    }

    static <T3> T3 any() {
        return null;
    }

    static <T4> void when(T4 methodCall) {
    }
}
