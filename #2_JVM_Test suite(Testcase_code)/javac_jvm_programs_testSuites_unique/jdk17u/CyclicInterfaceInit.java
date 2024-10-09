public class CyclicInterfaceInit {

    interface Base {

        static final Object CONST = new Target() {
        }.someMethod();

        default void important() {
        }
    }

    static boolean out(String c) {
        System.out.println("initializing " + c);
        return true;
    }

    interface Target extends Base {

        boolean v = CyclicInterfaceInit.out("Target");

        default Object someMethod() {
            throw new RuntimeException();
        }
    }

    static class InnerBad implements Target {
    }

    public static void main(String[] args) {
        try {
            new Target() {
            };
        } catch (ExceptionInInitializerError e) {
            System.out.println("ExceptionInInitializerError thrown as expected");
        }
        try {
            InnerBad ig = new InnerBad();
            throw new RuntimeException("FAILED- initialization of InnerBad should throw NCDFE");
        } catch (NoClassDefFoundError e) {
            System.out.println("NoClassDefFoundError thrown as expected");
        }
        System.out.println("Target.v is " + Target.v);
    }
}
