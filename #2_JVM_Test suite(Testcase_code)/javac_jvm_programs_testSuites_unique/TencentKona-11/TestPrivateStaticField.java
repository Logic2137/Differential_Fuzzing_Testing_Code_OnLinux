



public class TestPrivateStaticField {

    
    private static int priv_field;

    
    public TestPrivateStaticField() {}

    
    
    
    

    
    
    

    void access_priv(TestPrivateStaticField o) {
        priv_field = o.priv_field++;
    }
    void access_priv(StaticNested o) {
        priv_field = o.priv_field++;
    }

    

    static interface StaticIface {

        

        default void access_priv(TestPrivateStaticField o) {
            int priv_field = o.priv_field++;
        }
        default void access_priv(StaticNested o) {
            int priv_field = o.priv_field++;
        }
    }

    static class StaticNested {

        private static int priv_field;

        
        public StaticNested() {}

        

        void access_priv(TestPrivateStaticField o) {
            priv_field = o.priv_field++;
        }
        void access_priv(StaticNested o) {
            priv_field = o.priv_field++;
        }
    }

    class InnerNested {

        
        public InnerNested() {}

        void access_priv(TestPrivateStaticField o) {
            int priv_field = o.priv_field++;
        }
        void access_priv(StaticNested o) {
            int priv_field = o.priv_field++;
        }
    }

    public static void main(String[] args) {
        TestPrivateStaticField o = new TestPrivateStaticField();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestPrivateStaticField());
        o.access_priv(s);

        s.access_priv(o);
        s.access_priv(new StaticNested());

        i.access_priv(o);
        i.access_priv(s);

        intf.access_priv(o);
        intf.access_priv(s);
    }
}
