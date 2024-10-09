



public class TestInvokeStatic {

    
    private static void priv_static_invoke() {
        System.out.println("TestInvokeStatic::priv_static_invoke");
    }

    
    public TestInvokeStatic() {}

    
    
    
    

    
    

    void access_priv(TestInvokeStatic o) {
        o.priv_static_invoke();
    }
    void access_priv(StaticNested o) {
        o.priv_static_invoke();
    }
    void access_priv(StaticIface o) {
        StaticIface.priv_static_invoke();
    }

    

    static interface StaticIface {

        private static void priv_static_invoke() {
            System.out.println("StaticIface::priv_static_invoke");
        }

        

        default void access_priv(TestInvokeStatic o) {
            o.priv_static_invoke();
        }
        default void access_priv(StaticNested o) {
            o.priv_static_invoke();
        }
        default void access_priv(StaticIface o) {
            StaticIface.priv_static_invoke();
        }
    }

    static class StaticNested {

        private static void priv_static_invoke() {
            System.out.println("StaticNested::priv_static_invoke");
        }

        
        public StaticNested() {}

        

        void access_priv(TestInvokeStatic o) {
            o.priv_static_invoke();
        }
        void access_priv(StaticNested o) {
            o.priv_static_invoke();
        }
        void access_priv(StaticIface o) {
            StaticIface.priv_static_invoke();
        }
    }

    class InnerNested {

        
        public InnerNested() {}

        void access_priv(TestInvokeStatic o) {
            o.priv_static_invoke();
        }
        void access_priv(StaticNested o) {
            o.priv_static_invoke();
        }
        void access_priv(StaticIface o) {
            StaticIface.priv_static_invoke();
        }
    }

    public static void main(String[] args) {
        TestInvokeStatic o = new TestInvokeStatic();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestInvokeStatic());
        o.access_priv(s);
        o.access_priv(intf);

        s.access_priv(o);
        s.access_priv(new StaticNested());
        s.access_priv(intf);

        i.access_priv(o);
        i.access_priv(s);
        i.access_priv(intf);

        intf.access_priv(o);
        intf.access_priv(s);
        intf.access_priv(new StaticIface(){});
    }
}
