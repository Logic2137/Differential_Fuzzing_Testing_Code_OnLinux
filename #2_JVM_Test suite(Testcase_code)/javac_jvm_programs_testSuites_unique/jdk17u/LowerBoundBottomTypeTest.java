public class LowerBoundBottomTypeTest {

    void g() {
        f().getInIntf3().getInIntf2().getInIntf1().getA();
    }

    interface IntfA {

        int getA();
    }

    interface Intf1<A extends IntfA> {

        A getInIntf1();
    }

    interface Intf2<B> {

        Intf1<? extends B> getInIntf2();
    }

    interface Intf3<C> {

        Intf2<? extends C> getInIntf3();
    }

    Intf3<?> f() {
        return null;
    }
}
