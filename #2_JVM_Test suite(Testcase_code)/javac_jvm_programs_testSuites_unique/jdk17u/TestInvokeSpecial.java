public class TestInvokeSpecial {

    private TestInvokeSpecial() {
    }

    static interface StaticIface {

        default void doConstruct(TestInvokeSpecial o) {
            Object obj = new TestInvokeSpecial();
        }

        default void doConstruct(InnerNested o) {
            Object obj = new TestInvokeSpecial().new InnerNested();
        }

        default void doConstruct(StaticNested o) {
            Object obj = new StaticNested();
        }

        default void doConstruct(StaticIface o) {
            Object obj = new StaticIface() {
            };
        }
    }

    static class StaticNested {

        private StaticNested() {
        }

        public void doConstruct(TestInvokeSpecial o) {
            Object obj = new TestInvokeSpecial();
        }

        public void doConstruct(InnerNested o) {
            Object obj = new TestInvokeSpecial().new InnerNested();
        }

        public void doConstruct(StaticNested o) {
            Object obj = new StaticNested();
        }

        public void doConstruct(StaticIface o) {
            Object obj = new StaticIface() {
            };
        }
    }

    class InnerNested {

        private InnerNested() {
        }

        public void doConstruct(TestInvokeSpecial o) {
            Object obj = new TestInvokeSpecial();
        }

        public void doConstruct(InnerNested o) {
            Object obj = new TestInvokeSpecial().new InnerNested();
        }

        public void doConstruct(StaticNested o) {
            Object obj = new StaticNested();
        }

        public void doConstruct(StaticIface o) {
            Object obj = new StaticIface() {
            };
        }
    }

    public static void main(String[] args) {
        TestInvokeSpecial o = new TestInvokeSpecial();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {
        };
        s.doConstruct(o);
        s.doConstruct(i);
        s.doConstruct(s);
        s.doConstruct(intf);
        i.doConstruct(o);
        i.doConstruct(i);
        i.doConstruct(s);
        i.doConstruct(intf);
        intf.doConstruct(o);
        intf.doConstruct(i);
        intf.doConstruct(s);
        intf.doConstruct(intf);
    }
}
