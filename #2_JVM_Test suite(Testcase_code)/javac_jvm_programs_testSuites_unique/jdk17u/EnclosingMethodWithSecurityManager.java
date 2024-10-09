public class EnclosingMethodWithSecurityManager {

    public static void main(String[] args) {
        if (args.length == 1) {
            System.setSecurityManager(new SecurityManager());
        }
        new Inner().setTheInner();
        Inner.theInner.getEnclosingMethod();
    }

    public static class Inner {

        public static Class<?> theInner;

        public void setTheInner() {
            Object o = new Object() {
            };
            Inner.theInner = o.getClass();
        }
    }
}
