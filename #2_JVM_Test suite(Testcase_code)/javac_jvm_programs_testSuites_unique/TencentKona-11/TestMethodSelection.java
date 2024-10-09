










import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import java.lang.reflect.InvocationTargetException;

public class TestMethodSelection {

    static final MethodType M_T = MethodType.methodType(String.class);

    static class A {
        public String m() { return "A::m"; }
    }
    static class PA {
        private String m() { return "PA::m"; }
    }

    static class B_A extends A {
        public String m() { return "B_A::m"; }
    }
    static class B_PA extends PA {
        public String m() { return "B_PA::m"; }
    }
    
    static class PB_A extends A {
        public String m() { return "PB_A::m"; }
    }
    static class PB_PA extends PA {
        private String m() { return "PB_PA::m"; }
    }

    static class C_B_A extends B_A {
        public String m() { return "C_B_A::m"; }
    }
    
    static class PC_B_A extends B_A {
        public String m() { return "PC_B_A"; }
    }
    static class C_PB_A extends PB_A {
        public String m() { return "C_PB_A::m"; }
    }
    
    static class PC_PB_A extends PB_A {
        public String m() { return "PC_PB_A"; }
    }
    static class C_B_PA extends B_PA {
        public String m() { return "C_B_PA::m"; }
    }
    
    static class PC_B_PA extends B_PA {
        public String m() { return "PC_B_PA"; }
    }
    static class C_PB_PA extends PB_PA {
        public String m() { return "C_PB_PA::m"; }
    }
    static class PC_PB_PA extends PB_PA {
        private String m() { return "PC_PB_PA::m"; }
    }

    

    static void doInvoke(B_A target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(B_A.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)B_A.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }
    static void doInvoke(B_PA target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(B_PA.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)B_PA.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }
    static void doInvoke(PB_A target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(PB_A.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)PB_A.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }
    static void doInvoke(PB_PA target, String expected) throws Throwable {
        
        check(target.m(), expected);
        
        MethodHandle mh = lookup().findVirtual(PB_PA.class, "m", M_T);
        check((String)mh.invoke(target), expected);
        
        check((String)PB_PA.class.getDeclaredMethod("m", new Class<?>[0]).
              invoke(target, new Object[0]), expected);
    }

    static void check(String actual, String expected) {
        if (!actual.equals(expected)) {
                throw new Error("Selection error: expected " + expected +
                                " but got " + actual);
        }
    }

    public static void main(String[] args) throws Throwable {
        
        doInvoke(new PB_PA(), "PB_PA::m");
        doInvoke(new B_PA(),  "B_PA::m");
        doInvoke(new PB_A(),  "PB_A::m");
        doInvoke(new B_A(),   "B_A::m");
        
        doInvoke(new PC_PB_PA(), "PB_PA::m");
        doInvoke(new C_PB_PA(),  "PB_PA::m");
        doInvoke(new PC_B_PA(),  "B_PA::m");
        doInvoke(new C_B_PA(),   "C_B_PA::m");
        doInvoke(new PC_PB_A(),  "PB_A::m");
        doInvoke(new C_PB_A(),   "PB_A::m");
        doInvoke(new PC_B_A(),   "B_A::m");
        doInvoke(new C_B_A(),    "C_B_A::m");
    }
}




