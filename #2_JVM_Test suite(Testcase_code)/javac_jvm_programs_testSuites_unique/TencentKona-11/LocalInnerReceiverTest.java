



class LocalInnerReceiverTest {
    void m() {
        class Inner {
            Inner(LocalInnerReceiverTest LocalInnerReceiverTest.this) {}
        }
    }
}
