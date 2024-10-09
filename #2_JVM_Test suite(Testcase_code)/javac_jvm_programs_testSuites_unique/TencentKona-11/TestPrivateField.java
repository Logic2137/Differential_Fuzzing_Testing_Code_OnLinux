



public class TestPrivateField {

    
    private int priv_field;

    
    public TestPrivateField() {}

    

    

    void access_priv(TestPrivateField o) {
        this.priv_field = o.priv_field++;
    }
    void access_priv(InnerNested o) {
        this.priv_field = o.priv_field++;
    }
    void access_priv(StaticNested o) {
        this.priv_field = o.priv_field++;
    }

    

    static interface StaticIface {

        

        default void access_priv(TestPrivateField o) {
            int priv_field = o.priv_field++;
        }
        default void access_priv(InnerNested o) {
            int priv_field = o.priv_field++;
        }
        default void access_priv(StaticNested o) {
            int priv_field = o.priv_field++;
        }
    }

    static class StaticNested {

        private int priv_field;

        
        public StaticNested() {}

        

        void access_priv(TestPrivateField o) {
            this.priv_field = o.priv_field++;
        }
        void access_priv(InnerNested o) {
            this.priv_field = o.priv_field++;
        }
        void access_priv(StaticNested o) {
            this.priv_field = o.priv_field++;
        }
    }

    class InnerNested {

        private int priv_field;

        
        public InnerNested() {}

        void access_priv(TestPrivateField o) {
            this.priv_field = o.priv_field++;
        }
        void access_priv(InnerNested o) {
            this.priv_field = o.priv_field++;
        }
        void access_priv(StaticNested o) {
            this.priv_field = o.priv_field++;
        }
    }

    public static void main(String[] args) {
        TestPrivateField o = new TestPrivateField();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestPrivateField());
        o.access_priv(i);
        o.access_priv(s);

        s.access_priv(o);
        s.access_priv(i);
        s.access_priv(new StaticNested());

        i.access_priv(o);
        i.access_priv(o.new InnerNested());
        i.access_priv(s);

        intf.access_priv(o);
        intf.access_priv(i);
        intf.access_priv(s);
    }
}
