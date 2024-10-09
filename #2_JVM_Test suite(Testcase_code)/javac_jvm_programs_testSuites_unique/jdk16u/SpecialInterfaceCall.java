



import java.lang.invoke.*;

public class SpecialInterfaceCall {
    interface I1 {
        default void pub_m() {};
        private void priv_m() {};
    }
    interface I2 extends I1 {
        
        
        
        default void pub_m() {};

        private void priv_m() {};

        static void invokeDirect(I2 i) {
            i.priv_m(); 
        }
        static void invokeSpecialMH(I2 i) throws Throwable {
            
            mh_I2_priv_m_from_I2.invokeExact(i);
        }
        
        static void invokeSpecialObjectMH(I2 i) throws Throwable {
            
            
            String s = (String) mh_I1_toString_from_I2.invokeExact(i);
        }
        
        static void invokeSpecialObjectFinalMH(I2 i) throws Throwable {
            
            
            Class<?> c = (Class<?>) mh_I1_getClass_from_I2.invokeExact(i);
        }
    }
    interface I3 extends I2 {
        
        
        
        static void invokeSpecialMH(I3 i) throws Throwable {
            
            mh_I2_pub_m_from_I3.invokeExact(i);
        }
    }
    
    
    
    interface I4 extends I1 {
        static void invokeDirect(I4 i) {
            
            throw new Error("Class file for I4 is not overwritten");
        }
        static void invokeDirectFinal(I4 i) {
            
            throw new Error("Class file for I4 is not overwritten");
        }
    }

    
    static class C1 implements I1 { }
    static class C2 implements I2 { }
    static class C3 implements I3 { }
    static class C4 implements I4 { }

    
    
    static class D1 implements I1 { }
    static class E {
        public void pub_m() {}
        private void priv_m() {}
    }

    
    static final MethodHandle mh_I2_priv_m_from_I2;

    
    static final MethodHandle mh_I2_pub_m_from_I3;

    
    static final MethodHandle mh_I1_toString_from_I2;

    
    static final MethodHandle mh_I1_getClass_from_I2;

    static {
        try {
            MethodType mt = MethodType.methodType(void.class);
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            mh_I2_priv_m_from_I2 = lookup.findSpecial(I2.class, "priv_m", mt, I2.class);
            mh_I2_pub_m_from_I3 = lookup.findSpecial(I2.class, "pub_m", mt, I3.class);

            mt = MethodType.methodType(String.class);
            mh_I1_toString_from_I2 = lookup.findSpecial(I1.class, "toString", mt, I2.class);

            mt = MethodType.methodType(Class.class);
            mh_I1_getClass_from_I2 = lookup.findSpecial(I1.class, "getClass", mt, I2.class);

        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    static void runPositiveTests() {
        shouldNotThrow(() -> I2.invokeDirect(new C2()));
        shouldNotThrow(() -> I2.invokeDirect(new C3()));
        shouldNotThrow(() -> I2.invokeSpecialMH(new C2()));
        shouldNotThrow(() -> I2.invokeSpecialMH(new C3()));
        shouldNotThrow(() -> I2.invokeSpecialObjectMH(new C2()));
        shouldNotThrow(() -> I2.invokeSpecialObjectMH(new C3()));
        shouldNotThrow(() -> I2.invokeSpecialObjectFinalMH(new C2()));
        shouldNotThrow(() -> I2.invokeSpecialObjectFinalMH(new C3()));

        shouldNotThrow(() -> I3.invokeSpecialMH(new C3()));

        shouldNotThrow(() -> I4.invokeDirect(new C4()));
        shouldNotThrow(() -> I4.invokeDirectFinal(new C4()));
    }

    static void runNegativeTests() {
        System.out.println("IAE I2.invokeDirect D1");
        shouldThrowIAE(() -> I2.invokeDirect(unsafeCastI2(new D1())));
        System.out.println("IAE I2.invokeDirect E");
        shouldThrowIAE(() -> I2.invokeDirect(unsafeCastI2(new E())));
        System.out.println("ICCE I2.invokeMH D1");
        shouldThrowICCE(() -> I2.invokeSpecialMH(unsafeCastI2(new D1())));
        System.out.println("ICCE I2.invokeMH E");
        shouldThrowICCE(() -> I2.invokeSpecialMH(unsafeCastI2(new E())));
        System.out.println("ICCE I3.invokeMH D1");
        shouldThrowICCE(() -> I3.invokeSpecialMH(unsafeCastI3(new D1())));
        System.out.println("ICCE I3.invokeMH E");
        shouldThrowICCE(() -> I3.invokeSpecialMH(unsafeCastI3(new E())));
        System.out.println("ICCE I3.invokeMH C2");
        shouldThrowICCE(() -> I3.invokeSpecialMH(unsafeCastI3(new C2())));
        System.out.println("ICCE I4.invokeDirect C1");
        shouldThrowIAE(() -> I4.invokeDirect(unsafeCastI4(new C1())));
        System.out.println("ICCE I4.invokeDirectFinal C1");
        shouldThrowIAE(() -> I4.invokeDirectFinal(unsafeCastI4(new C1())));
        System.out.println("ICCE I2.invokeObjectMH C1");
        shouldThrowICCE(() -> I2.invokeSpecialObjectMH(unsafeCastI2(new C1())));
        System.out.println("ICCE I2.invokeObjectFinalMH C1");
        shouldThrowICCE(() -> I2.invokeSpecialObjectFinalMH(unsafeCastI2(new C1())));

    }

    static void warmup() {
        for (int i = 0; i < 20_000; i++) {
            runPositiveTests();
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("UNRESOLVED:");
        runNegativeTests();
        runPositiveTests();

        System.out.println("RESOLVED:");
        runNegativeTests();

        System.out.println("WARMUP:");
        warmup();

        System.out.println("COMPILED:");
        runNegativeTests();
        runPositiveTests();
    }

    static interface Test {
        void run() throws Throwable;
    }

    static void shouldThrowICCE(Test t) {
        shouldThrow(IncompatibleClassChangeError.class,
                    "is not a subclass of caller class", t);
    }

    static void shouldThrowIAE(Test t) {
        shouldThrow(IllegalAccessError.class,
                    "must be the current class or a subtype of interface", t);
    }

    static void shouldThrow(Class<?> expectedError, String reason, Test t) {
        try {
            t.run();
        } catch (Throwable e) {
            if (expectedError.isInstance(e)) {
                if (e.getMessage().contains(reason)) {
                    
                    System.out.println("Threw expected: " + e);
                    return;
                }
                else {
                    throw new AssertionError("Wrong exception reason: expected '" + reason
                                             + "', got '" + e.getMessage() + "'", e);
                }
            } else {
                String msg = String.format("Wrong exception thrown: expected=%s; thrown=%s",
                                           expectedError.getName(), e.getClass().getName());
                throw new AssertionError(msg, e);
            }
        }
        throw new AssertionError("No exception thrown: expected " + expectedError.getName());
    }

    static void shouldNotThrow(Test t) {
        try {
            t.run();
            
        } catch (Throwable e) {
            throw new AssertionError("Exception was thrown: ", e);
        }
    }

    

    static I2 unsafeCastI2(Object obj) {
        try {
            MethodHandle mh = MethodHandles.identity(Object.class);
            mh = MethodHandles.explicitCastArguments(mh, mh.type().changeReturnType(I2.class));
            return (I2)mh.invokeExact((Object) obj);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    static I3 unsafeCastI3(Object obj) {
        try {
            MethodHandle mh = MethodHandles.identity(Object.class);
            mh = MethodHandles.explicitCastArguments(mh, mh.type().changeReturnType(I3.class));
            return (I3)mh.invokeExact((Object) obj);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    static I4 unsafeCastI4(Object obj) {
        try {
            MethodHandle mh = MethodHandles.identity(Object.class);
            mh = MethodHandles.explicitCastArguments(mh, mh.type().changeReturnType(I4.class));
            return (I4)mh.invokeExact((Object) obj);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }
}
