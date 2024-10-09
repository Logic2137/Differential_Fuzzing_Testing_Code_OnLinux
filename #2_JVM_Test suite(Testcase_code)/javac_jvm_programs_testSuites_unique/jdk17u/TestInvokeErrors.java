public class TestInvokeErrors {

    static class Nested {

        private void priv_invoke() {
            System.out.println("Nested::priv_invoke");
        }
    }

    static class MissingMethod {

        private void priv_invoke() {
            System.out.println("MissingMethod::priv_invoke");
        }
    }

    static class MissingMethodWithSuper extends Nested {

        private void priv_invoke() {
            System.out.println("MissingMethodWithSuper::priv_invoke");
        }
    }

    static class MissingNestHost {

        private void priv_invoke() {
            System.out.println("MissingNestHost::priv_invoke");
        }
    }

    static class Helper {

        static void doTest() {
            try {
                MissingNestHost m = new MissingNestHost();
                m.priv_invoke();
                throw new Error("Unexpected success invoking MissingNestHost.priv_invoke");
            } catch (IllegalAccessError iae) {
                if (iae.getMessage().contains("java.lang.NoClassDefFoundError: NoSuchClass")) {
                    System.out.println("Got expected exception:" + iae);
                } else {
                    throw new Error("Unexpected exception", iae);
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        boolean verifying = Boolean.parseBoolean(args[0]);
        System.out.println("Verification is " + (verifying ? "enabled" : "disabled"));
        try {
            MissingMethod m = new MissingMethod();
            m.priv_invoke();
            throw new Error("Unexpected success invoking MissingMethod.priv_invoke");
        } catch (NoSuchMethodError nsme) {
            System.out.println("Got expected exception:" + nsme);
        }
        MissingMethodWithSuper m = new MissingMethodWithSuper();
        m.priv_invoke();
        try {
            Helper.doTest();
        } catch (IllegalAccessError iae) {
            if (verifying)
                System.out.println("Got expected exception:" + iae);
            else
                throw new Error("Unexpected error loading Helper class with verification disabled", iae);
        }
    }
}
