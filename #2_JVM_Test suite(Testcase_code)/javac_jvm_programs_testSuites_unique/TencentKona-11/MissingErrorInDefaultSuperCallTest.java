

public class MissingErrorInDefaultSuperCallTest {
    interface I {
        default int f(){return 0;}
    }

    class J implements I {}

    class T extends J implements I {
        public int f() {
            return I.super.f();
        }
    }
}
