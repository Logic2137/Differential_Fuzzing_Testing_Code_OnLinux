public class CtorAccessBypassTest {

    public static void main(String[] args) {
        new CtorAccessBypassTest_01<Object>(null) {
        };
        new CtorAccessBypassTest_01<>(null) {
        };
    }
}

class CtorAccessBypassTest_01<T> {

    private class Private {
    }

    CtorAccessBypassTest_01(Private p) {
    }
}
