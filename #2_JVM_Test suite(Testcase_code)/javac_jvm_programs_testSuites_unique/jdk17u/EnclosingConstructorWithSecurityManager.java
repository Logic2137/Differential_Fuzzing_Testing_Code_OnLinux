public class EnclosingConstructorWithSecurityManager {

    public static void main(String[] args) {
        if (args.length == 1) {
            System.setSecurityManager(new SecurityManager());
        }
        new Inner();
        Inner.theInner.getEnclosingConstructor();
    }

    public static class Inner {

        public static Class<?> theInner;

        public Inner() {
            Object o = new Object() {
            };
            Inner.theInner = o.getClass();
        }
    }
}
