



public class TestInvoke {

    
    private void priv_invoke() {
        System.out.println("TestInvoke::priv_invoke");
    }

    
    public TestInvoke() {}

    

    void access_priv(TestInvoke o) {
        o.priv_invoke();
    }
    void access_priv(InnerNested o) {
        o.priv_invoke();
    }
    void access_priv(StaticNested o) {
        o.priv_invoke();
    }
    void access_priv(StaticIface o) {
        o.priv_invoke();
    }

    

    static interface StaticIface {

        private void priv_invoke() {
            System.out.println("StaticIface::priv_invoke");
        }

        

        default void access_priv(TestInvoke o) {
            o.priv_invoke();
        }
        default void access_priv(InnerNested o) {
            o.priv_invoke();
        }
        default void access_priv(StaticNested o) {
            o.priv_invoke();
        }
        default void access_priv(StaticIface o) {
            o.priv_invoke();
        }
    }

    static class StaticNested {

        private void priv_invoke() {
            System.out.println("StaticNested::priv_invoke");
        }

        
        public StaticNested() {}

        

        void access_priv(TestInvoke o) {
            o.priv_invoke();
        }
        void access_priv(InnerNested o) {
            o.priv_invoke();
        }
        void access_priv(StaticNested o) {
            o.priv_invoke();
        }
        void access_priv(StaticIface o) {
            o.priv_invoke();
        }
    }

    class InnerNested {

        private void priv_invoke() {
            System.out.println("InnerNested::priv_invoke");
        }

        
        public InnerNested() {}

        void access_priv() {
            TestInvoke.this.priv_invoke(); 
        }
        void access_priv(TestInvoke o) {
            o.priv_invoke();
        }
        void access_priv(InnerNested o) {
            o.priv_invoke();
        }
        void access_priv(StaticNested o) {
            o.priv_invoke();
        }
        void access_priv(StaticIface o) {
            o.priv_invoke();
        }
    }

    public static void main(String[] args) {
        TestInvoke o = new TestInvoke();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestInvoke());
        o.access_priv(i);
        o.access_priv(s);
        o.access_priv(intf);

        s.access_priv(o);
        s.access_priv(i);
        s.access_priv(new StaticNested());
        s.access_priv(intf);

        i.access_priv();
        i.access_priv(o);
        i.access_priv(o.new InnerNested());
        i.access_priv(s);
        i.access_priv(intf);

        intf.access_priv(o);
        intf.access_priv(i);
        intf.access_priv(s);
        intf.access_priv(new StaticIface(){});
    }
}
