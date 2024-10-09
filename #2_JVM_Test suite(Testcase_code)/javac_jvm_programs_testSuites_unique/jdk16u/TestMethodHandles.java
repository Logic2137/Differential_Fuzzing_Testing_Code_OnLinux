




import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

public class TestMethodHandles {

    static final MethodType INVOKE_T = MethodType.methodType(void.class);

    
    private static void priv_static_invoke() {
        System.out.println("TestMethodHandles::priv_static_invoke");
    }

    
    public TestMethodHandles() {}

    

    
    

    void access_priv(TestMethodHandles o) throws Throwable {
        MethodHandle mh =
          lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
        mh.invoke();
        mh.invokeExact();
    }
    void access_priv(StaticNested o) throws Throwable {
        MethodHandle mh =
          lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
        mh.invoke();
        mh.invokeExact();
    }
    void access_priv(StaticIface o) throws Throwable {
        MethodHandle mh =
          lookup().findStatic(StaticIface.class, "priv_static_invoke", INVOKE_T);
        mh.invoke();
        mh.invokeExact();
    }

    

    static interface StaticIface {

        private static void priv_static_invoke() {
            System.out.println("StaticIface::priv_static_invoke");
        }

        

        default void access_priv(TestMethodHandles o) throws Throwable {
          MethodHandle mh =
            lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
        default void access_priv(StaticNested o) throws Throwable {
          MethodHandle mh =
            lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
        default void access_priv(StaticIface o) throws Throwable {
          MethodHandle mh =
            lookup().findStatic(StaticIface.class, "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
    }

    static class StaticNested {

        private static void priv_static_invoke() {
            System.out.println("StaticNested::priv_static_invoke");
        }

        
        public StaticNested() {}

        

        void access_priv(TestMethodHandles o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
        void access_priv(StaticNested o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
        void access_priv(StaticIface o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(StaticIface.class, "priv_static_invoke", INVOKE_T);
          mh.invoke();
          mh.invokeExact();
        }
    }

    class InnerNested {

        
        public InnerNested() {}

        void access_priv(TestMethodHandles o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
            mh.invoke();
            mh.invokeExact();
        }
        void access_priv(StaticNested o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(o.getClass(), "priv_static_invoke", INVOKE_T);
            mh.invoke();
            mh.invokeExact();
        }
        void access_priv(StaticIface o) throws Throwable {
            MethodHandle mh =
              lookup().findStatic(StaticIface.class, "priv_static_invoke", INVOKE_T);
            mh.invoke();
            mh.invokeExact();
        }
    }

    public static void main(String[] args) throws Throwable {
        TestMethodHandles o = new TestMethodHandles();
        StaticNested s = new StaticNested();
        InnerNested i = o.new InnerNested();
        StaticIface intf = new StaticIface() {};

        o.access_priv(new TestMethodHandles());
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
