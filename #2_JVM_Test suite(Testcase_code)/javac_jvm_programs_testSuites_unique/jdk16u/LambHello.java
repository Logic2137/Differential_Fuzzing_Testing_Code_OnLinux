
public class LambHello {
    public static void main(String[] args) {
        doTest();
    }

    static void doTest() {
        doit(() -> {
            System.out.println("Hello from doTest");
        });
    }
    static void doit(Runnable t) {
        t.run();
    }
}
