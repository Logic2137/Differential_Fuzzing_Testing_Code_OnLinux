



import java.util.function.Supplier;

public class InnerInstanceCreationTest {

    static String cookie = "";

    public static void main(String[] args) {
        new InnerInstanceCreationTest().new Producer();
        new InnerInstanceCreationTest().new Producer(0);
        new InnerInstanceCreationTest().new Producer("");
        if (!cookie.equals("BlahBlahBlah"))
            throw new AssertionError("Unexpected cookie");
    }

    class Inner {
        Inner() {
            cookie += "Blah";
        }
    }

    class Producer {
        Producer() {
            this(Inner::new);
        }
        Producer(int x) {
            this(() -> new Inner());
        }
        Producer(String s) {
            this(() -> InnerInstanceCreationTest.this.new Inner());
        }
        Producer(Supplier<Object> supplier) {
            supplier.get();
        }
    }
}
