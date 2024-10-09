public class RegisterNatives {

    interface I {

        void registerNatives();
    }

    interface J extends I {
    }

    interface K {

        default public void registerNatives() {
            System.out.println("K");
        }
    }

    static class B implements J {

        public void registerNatives() {
            System.out.println("B");
        }
    }

    static class C implements K {
    }

    public static void main(String... args) {
        System.out.println("Regression test for JDK-8024804, crash when InterfaceMethodref resolves to Object.registerNatives\n");
        J val = new B();
        try {
            val.registerNatives();
        } catch (IllegalAccessError e) {
            System.out.println("TEST FAILS - JDK 8 JVMS, static and non-public methods of j.l.Object should be ignored during interface method resolution\n");
            e.printStackTrace();
            throw e;
        }
        C cval = new C();
        try {
            cval.registerNatives();
        } catch (IllegalAccessError e) {
            System.out.println("TEST FAILS - a default method named registerNatives should no longer be masked by removed Object.registerNatives\n");
            e.printStackTrace();
            throw e;
        }
        System.out.println("TEST PASSES - no IAE resulted\n");
    }
}
