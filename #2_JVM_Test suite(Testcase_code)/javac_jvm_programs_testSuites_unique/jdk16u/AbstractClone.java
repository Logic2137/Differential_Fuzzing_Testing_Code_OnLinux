



public class AbstractClone {

    interface I1 {
        Object clone();
    }

    interface I2 extends I1 { }

    static class C implements I2 {
        public Object clone() {
            return "In C's clone()";
        }
    }

    static Object test(I2 i) { return i.clone(); }

    public static void main(String[] args) {
        String s = (String)test(new C());
        if (!s.equals("In C's clone()")) {
            throw new RuntimeException("Wrong clone() called");
        }
    }
}
