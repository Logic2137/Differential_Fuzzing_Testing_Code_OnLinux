









public class TestReflection {

    
    private static void priv_static_invoke() {
        System.out.println("TestReflection::priv_static_invoke");
    }

    
    public TestReflection() {}

    

    
    

    void access_priv(TestReflection o) throws Throwable {
        o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
    }
    void access_priv(StaticNested o) throws Throwable {
        o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
    }
    void access_priv(StaticIface o) throws Throwable {
        
        StaticIface.class.getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
    }

    

    static interface StaticIface {

        private static void priv_static_invoke() {
            System.out.println("StaticIface::priv_static_invoke");
        }

        

        default void access_priv(TestReflection o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        default void access_priv(StaticNested o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        default void access_priv(StaticIface o) throws Throwable {
            
            StaticIface.class.getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
    }

    static class StaticNested {

        private static void priv_static_invoke() {
            System.out.println("StaticNested::priv_static_invoke");
        }

        
        public StaticNested() {}

        

        void access_priv(TestReflection o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        void access_priv(StaticNested o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        void access_priv(StaticIface o) throws Throwable {
            
            StaticIface.class.getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
    }

    class InnerNested {

        
        public InnerNested() {}

        void access_priv(TestReflection o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        void access_priv(StaticNested o) throws Throwable {
            o.getClass().getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
        void access_priv(StaticIface o) throws Throwable {
            
            StaticIface.class.getDeclaredMethod("priv_static_invoke", new Class<?>[0]).invoke(null, new Object[0]);
        }
    }

    public static void main(String[] args) throws Throwable {
        TestReflection o = new TestReflection();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestReflection());
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
