
public class LambHello {
    public static void main(String[] args) {
    }

    public void doTest() {
        doit(() -> {
            System.out.println("Hello from doTest");
        });
    }
    static void doit(Runnable t) {
        t.run();
    }
}
