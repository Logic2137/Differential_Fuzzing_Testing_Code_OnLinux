










import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import java.lang.reflect.InvocationTargetException;

public class TestInterfaceMethodSelection {

    static final MethodType M_T = MethodType.methodType(String.class);

    static interface I {
        public String m();
    }
    static interface PI {
        private String m() { return "PI::m"; }
    }

    static class A_I implements I {
        public String m() { return "A_I::m"; }
    }
    static class A_PI implements PI {
        public String m() { return "A_PI::m"; }
    }
    
    static class PA_I implements I {
        private String real_m() { return "PA_I::m"; }
        public String m() { return "Should not see this"; }
    }
    static class PA_PI implements PI {
        private String m() { return "PA_PI::m"; }
    }

    static class B_A_I extends A_I {
        public String m() { return "B_A_I::m"; }
    }
    
    static class PB_A_I extends A_I {
        public String m() { return "PB_A_I::m"; }
    }
    static class B_PA_I extends PA_I {
        public String m() { return "B_PA_I::m"; }
    }
    
    static class PB_PA_I extends PA_I {
        public String m() { return "Should not see this"; }
        private String real_m() { return "PB_PA_I"; }
    }
    static class B_A_PI extends A_PI {
        public String m() { return "B_A_PI::m"; }
    }
    
    static class PB_A_PI extends A_PI {
        public String m() { return "PB_A_PI"; }
    }
    static class B_PA_PI extends PA_PI {
        public String m() { return "B_PA_PI::m"; }
    }
    static class PB_PA_PI extends PA_PI {
        private String m() { return "PB_PA_PI::m"; }
    }

    

    static void doInvoke(I target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(I.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)I.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }
    static void doInvoke(PI target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(PI.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)PI.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }

    static void badInvoke(I target) {
        badDirectInvoke(target);
        badMHInvoke(target);
        badReflectInvoke(target);
    }

    static void badDirectInvoke(I target) {
        try {
            target.m();
            throw new Error("Unexpected success directly invoking " +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError");
        }
        catch (AbstractMethodError expected) {
        }
        catch (Throwable t) {
            throw new Error("Unexpected exception directly invoking " +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError got: " + t);
        }
    }

    static void badMHInvoke(I target) {
        try {
            lookup().findVirtual(I.class, "m", M_T).invoke(target);
            throw new Error("Unexpected success for MH invoke of" +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError");
        }
        catch (AbstractMethodError expected) {
        }
        catch (Throwable t) {
            throw new Error("Unexpected exception for MH invoke of " +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError got: " + t);
        }
    }

    static void badReflectInvoke(I target) {
        try {
            I.class.getDeclaredMethod("m", new Class<?>[0]).
                invoke(target, new Object[0]);
            throw new Error("Unexpected success for Method invoke of" +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError");
        }
        catch (InvocationTargetException expected) {
            Throwable t = expected.getCause();
            if (!(t instanceof AbstractMethodError)) {
                throw new Error("Unexpected exception for Method invoke of " +
                                target.getClass().getSimpleName() +
                                ".m() - expected AbstractMethodError got: " + t);

            }
        }
        catch (Throwable t) {
            throw new Error("Unexpected exception for Method invoke of " +
                            target.getClass().getSimpleName() +
                            ".m() - expected AbstractMethodError got: " + t);
        }
    }

    static void check(String actual, String expected) {
        if (!actual.equals(expected)) {
                throw new Error("Selection error: expected " + expected +
                                " but got " + actual);
        }
    }

    public static void main(String[] args) throws Throwable {
        
        doInvoke(new PA_PI(), "PI::m");
        doInvoke(new A_PI(),  "PI::m");
        badInvoke(new PA_I());
        doInvoke(new A_I(),   "A_I::m");
        
        doInvoke(new PB_PA_PI(), "PI::m");
        doInvoke(new B_PA_PI(),  "PI::m");
        doInvoke(new PB_A_PI(),  "PI::m");
        doInvoke(new B_A_PI(),   "PI::m");
        badInvoke(new PB_PA_I());
        doInvoke(new B_PA_I(),   "B_PA_I::m");
        doInvoke(new PB_A_I(),   "A_I::m");
        doInvoke(new B_A_I(),    "B_A_I::m");
    }
}




