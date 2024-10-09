import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

public class TestPrivateLookup {

    static boolean compiledForNestmates;

    static class C {

        static class D {

            private void m() {
            }
        }

        static void test() throws Throwable {
            MethodType M_T = MethodType.methodType(void.class);
            Lookup l = lookup();
            try {
                MethodHandle mh = l.findVirtual(D.class, "m", M_T);
                if (compiledForNestmates) {
                    System.out.println("Lookup of D.m from C succeeded as expected with nestmates");
                } else {
                    throw new Error("Unexpected success when not compiled for nestmates!");
                }
            } catch (IllegalAccessException iae) {
                if (!compiledForNestmates) {
                    System.out.println("Lookup of D.m from C failed as expected without nestmates");
                } else {
                    throw new Error("Unexpected failure with nestmates", iae);
                }
            }
            l = l.in(D.class);
            try {
                MethodHandle mh = l.findVirtual(D.class, "m", M_T);
                System.out.println("Lookup of D.m from D succeeded as expected" + " with" + (compiledForNestmates ? "" : "out") + " nestmates");
            } catch (IllegalAccessException iae) {
                throw new Error("Lookup of D.m from D failed", iae);
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        compiledForNestmates = C.D.class.getNestHost() == TestPrivateLookup.class;
        boolean expectingNestmates = args.length == 0;
        if (compiledForNestmates && !expectingNestmates) {
            throw new Error("Test is being run incorrectly: " + "nestmates are being used but not expected");
        }
        if (expectingNestmates && !compiledForNestmates) {
            throw new Error("Test is being run incorrectly: " + "nestmates are expected but not being used");
        }
        System.out.println("Testing with" + (expectingNestmates ? "" : "out") + " nestmates");
        C.test();
    }
}
