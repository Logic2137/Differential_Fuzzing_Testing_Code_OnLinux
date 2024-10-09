interface Interface1 {

    public void foo();

    public int hashCode();
}

public class ItablesVtableTest implements Interface1 {

    public void foo() {
        System.out.println("ItablesVtableTest foo");
    }

    public int hashCode() {
        return 55;
    }

    public static void main(String[] unused) {
        ItablesVtableTest c = new ItablesVtableTest();
        c.foo();
        System.out.println("Interface1 hashCode " + c.hashCode());
    }
}
