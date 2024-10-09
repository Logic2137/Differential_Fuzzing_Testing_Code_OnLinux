import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

public class Test7157574 {

    interface Intf {

        void ig1();

        void ig2();

        void ig3();

        void ig4();

        void m1();
    }

    abstract static class Super implements Intf {

        public abstract void m2();

        public int f2;
    }

    abstract static class Sub extends Super {
    }

    public static void main(String... av) throws Throwable {
        MethodHandle m1 = lookup().findVirtual(Sub.class, "m1", methodType(void.class));
        System.out.println(m1);
        MethodHandle m2 = lookup().findVirtual(Sub.class, "m2", methodType(void.class));
        System.out.println(m2);
        MethodHandle f2 = lookup().findGetter(Sub.class, "f2", int.class);
        System.out.println(f2);
        MethodHandle f2s = lookup().findSetter(Sub.class, "f2", int.class);
        System.out.println(f2s);
        MethodHandle chc = lookup().findVirtual(Sub.class, "hashCode", methodType(int.class));
        System.out.println(chc);
        MethodHandle ihc = lookup().findVirtual(Intf.class, "hashCode", methodType(int.class));
        System.out.println(ihc);
        assertEquals(Sub.class, m1.type().parameterType(0));
        assertEquals(Sub.class, m2.type().parameterType(0));
        assertEquals(Sub.class, f2.type().parameterType(0));
        assertEquals(Sub.class, f2s.type().parameterType(0));
        assertEquals(Sub.class, chc.type().parameterType(0));
        assertEquals(Intf.class, ihc.type().parameterType(0));
        class C extends Sub {

            public void m1() {
                this.f2 = -1;
            }

            public void m2() {
                this.f2 = -2;
            }

            private void ig() {
                throw new RuntimeException();
            }

            public void ig1() {
                ig();
            }

            public void ig2() {
                ig();
            }

            public void ig3() {
                ig();
            }

            public void ig4() {
                ig();
            }
        }
        testConcrete(new C(), m1, m2, f2, f2s, chc, ihc);
    }

    private static void testConcrete(Sub s, MethodHandle m1, MethodHandle m2, MethodHandle f2, MethodHandle f2s, MethodHandle chc, MethodHandle ihc) throws Throwable {
        s.f2 = 0;
        m1.invokeExact(s);
        assertEquals(-1, s.f2);
        m2.invokeExact(s);
        assertEquals(-2, s.f2);
        s.f2 = 2;
        assertEquals(2, (int) f2.invokeExact(s));
        f2s.invokeExact(s, 0);
        assertEquals(0, s.f2);
        assertEquals(s.hashCode(), (int) chc.invokeExact(s));
        assertEquals(s.hashCode(), (int) ihc.invokeExact((Intf) s));
    }

    private static void assertEquals(Object expect, Object observe) {
        if (java.util.Objects.equals(expect, observe))
            return;
        String msg = ("expected " + expect + " but observed " + observe);
        System.out.println("FAILED: " + msg);
        throw new AssertionError(msg);
    }
}
