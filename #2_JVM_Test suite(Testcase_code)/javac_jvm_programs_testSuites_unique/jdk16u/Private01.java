


public class Private01 {
    interface P {
        private void foo() { System.out.println("foo!" + this); }
        default void m() {
           new Object() { void test() { foo(); } }.test();
        }
    }

    public static void main(String[] args) {
        P p = new P() {};
        p.m(); p.foo();
    }
}
