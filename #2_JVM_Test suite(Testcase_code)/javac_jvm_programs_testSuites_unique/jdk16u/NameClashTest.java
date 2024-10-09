



public class NameClashTest {

    String log = "";

    interface A1 {
        A1 m(String s);
    }

    abstract class A2 implements A1 {
        public abstract A2 m(String s);
    }

    interface B1 {
        A1 m(String s);
    }

    interface B2 extends B1 {
        A2 m(String s);
    }

    abstract class C extends A2 implements B2 {}

    class D extends C {

        public A2 m(String s) {
            log += s;
            return null;
        }
    }

    public static void main(String[] args) {
        NameClashTest nct = new NameClashTest();
        A1 a1 = nct.new D();
        a1.m("A1.m ");
        A2 a2 = nct.new D();
        a2.m("A2.m ");
        B1 b1 = nct.new D();
        b1.m("B1.m ");
        B2 b2 = nct.new D();
        b2.m("B2.m ");
        C c = nct.new D();
        c.m("C.m ");
        D d = nct.new D();
        d.m("D.m ");
        if (!nct.log.equals("A1.m A2.m B1.m B2.m C.m D.m "))
            throw new AssertionError("unexpected output: " + nct.log);
    }
}
